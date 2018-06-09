package cz.ophite.mimic.vhackos.botnet.service;

import cz.ophite.mimic.vhackos.botnet.Botnet;
import cz.ophite.mimic.vhackos.botnet.api.exception.ExploitException;
import cz.ophite.mimic.vhackos.botnet.api.exception.IpNotExistsException;
import cz.ophite.mimic.vhackos.botnet.api.module.*;
import cz.ophite.mimic.vhackos.botnet.api.net.response.BankResponse;
import cz.ophite.mimic.vhackos.botnet.api.net.response.MalwareKitResponse;
import cz.ophite.mimic.vhackos.botnet.api.net.response.TaskResponse;
import cz.ophite.mimic.vhackos.botnet.api.net.response.data.IpScanData;
import cz.ophite.mimic.vhackos.botnet.db.service.DatabaseService;
import cz.ophite.mimic.vhackos.botnet.service.base.EndpointService;
import cz.ophite.mimic.vhackos.botnet.service.base.IService;
import cz.ophite.mimic.vhackos.botnet.service.base.Service;
import cz.ophite.mimic.vhackos.botnet.servicemodule.ServiceModule;
import cz.ophite.mimic.vhackos.botnet.shared.dto.BruteState;
import cz.ophite.mimic.vhackos.botnet.shared.injection.Autowired;
import cz.ophite.mimic.vhackos.botnet.shared.injection.Inject;
import cz.ophite.mimic.vhackos.botnet.shared.utils.SharedUtils;
import cz.ophite.mimic.vhackos.botnet.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Služba pro správu sítě - skenování, prolamování banky, krádež peněz a čištění logů apod.
 *
 * @author mimic
 */
@Inject
@EndpointService(IService.SERVICE_NETWORK)
public final class NetworkService extends Service {

    private static final int MAX_EXPLOITS = 10;

    @Autowired
    private DatabaseService databaseService;

    @Autowired
    private BankModule bankModule;

    @Autowired
    private LogModule logModule;

    @Autowired
    private ServiceModule serviceModule;

    @Autowired
    private TaskModule taskModule;

    @Autowired
    private SdkModule sdkModule;

    @Autowired
    private MalwareModule malwareModule;

    protected NetworkService(Botnet botnet) {
        super(botnet);
    }

    @Override
    public String getDescription() {
        return "Exploit and steal money from the bank";
    }

    @Override
    protected void initialize() {
        setTimeout(getConfig().getNetworkTimeout());
    }

    @Override
    protected void execute() {
        var workData = new WorkData();

        if (getConfig().isNetworkStopAttackByBankMoney() && !isLowMoneyInBank(workData)) {
            getLog().info("There is enough money in our bank. Next attack will be in: {}", SharedUtils
                    .toTimeFormat(getTimeout()));
            return;
        }
        prepareWorkData(workData);
        getLog().info("Work data are: {}", workData);

        // nejprve zpracuje již rozpracované tásky
        tryProcessPreparedTasks(workData, false, workData.playerBankResponse == null);

        // vyhledá potencionální cíle na které se zautočí
        var targets = findPotencialTargets(workData);
        getLog().info("Targets to attack:");
        for (var ip : targets) {
            getLog().info("IP: {}, Level: {}, Firewall: {}", ip.getIp(), ip.getLevel(), ip.getFirewall());
        }
        var targetIps = targets.stream().map(IpScanData::getIp).collect(Collectors.toList());

        for (var ip : targetIps) {
            getLog().info("IP: {} - an attack begins", ip);
            sleep();
            tryExploitTarget(ip, workData);
        }
        var waitTime = tryProcessPreparedTasks(workData, true, true);

        if (waitTime < 0) {
            getLog().info("No bruteforce that could be attacked was found. Next attack will be in: {}", SharedUtils
                    .toTimeFormat(getTimeout()));
        } else {
            getLog().info("Network finished. Next attack will be in: {}", SharedUtils.toTimeFormat(getTimeout()));
        }
    }

    /**
     * Pokusí se zpracovat připravené tásky.
     */
    private long tryProcessPreparedTasks(WorkData workData, boolean reloadTasks, boolean reloadPlayerBank) {
        sleep();
        var waitTime = processPreparedTasks(workData, reloadTasks) * 1000;

        if (waitTime >= 0) {
            if (waitTime > 0) {
                getLog().info("Waiting for all the bruteforces to finish: {}", SharedUtils.toTimeFormat(waitTime));
                sleep(waitTime); // počká, až se všechny bruteforce dokončí
            }
            getLog().info("Start the bank robbery");
            processBrutedIps(workData, reloadTasks, reloadPlayerBank);
        }
        return waitTime;
    }

    /**
     * Chybí naší bance peníze?
     */
    private boolean isLowMoneyInBank(WorkData workData) {
        workData.playerBankResponse = bankModule.getBank();
        // v naší bance je dostatčné množství peněz
        if (workData.playerBankResponse.getTotal() >= getConfig().getNetworkUserBankLimit()) {
            return false;
        } else {
            sleep();
            return true;
        }
    }

    /**
     * Připraví pracovní data a podklad pro útok.
     */
    private void prepareWorkData(WorkData workData) {
        var tasks = taskModule.getTasks();
        sleep();
        var sdk = sdkModule.getSdk();

        workData.level = tasks.getLevel();
        workData.sdk = sdk.getSdk();
        workData.exploitsLeft = sdk.getExploits();
        workData.taskResponse = tasks;

        for (var ip : tasks.getBrutedIps()) {
            workData.brutedIps.add(ip.getIp());
        }
    }

    /**
     * Vyhledá potencionální cíle k útoku.
     */
    private List<IpScanData> findPotencialTargets(WorkData workData) {
        var targets = new ArrayList<IpScanData>();

        while (targets.size() < MAX_EXPLOITS) {
            sleep();
            var ips = serviceModule.scan();
            var levelRange = getConfig().getNetworkTargetLevelRange();

            for (var ip : ips.getIps()) {
                // vyhledá takové IP, které je možné exploitnout
                if (ip.getFirewall() < workData.sdk && ip.getLevel() >= levelRange.get(0) && ip.getLevel() <= levelRange
                        .get(1)) {

                    // IP nesmí být v bruteforce seznamu
                    if (!workData.brutedIps.contains(ip.getIp())) {
                        targets.add(ip);

                        if (targets.size() == MAX_EXPLOITS) {
                            break;
                        }
                    }
                }
            }
            if (targets.isEmpty()) {
                getLog().info("No satisfactory target for attack was found. The search will resume");
            }
        }
        return targets;
    }

    /**
     * Pokusí se exploitnou cíl.
     */
    private boolean tryExploitTarget(String ip, WorkData workData) {
        try {
            if (workData.exploitsLeft == 0) {
                getLog().info("IP: {} - no more exploits are available", ip);
                return false;
            }
            serviceModule.exploit(ip);
            workData.exploitsLeft--;
            getLog().info("IP: {} - exploit was successful", ip);
            sleep();

            var resp = serviceModule.getSystemInfo(ip);
            getLog().info("IP: {} - the remote user is '{}' with level: {}", ip, resp.getUserName(), resp.getLevel());
            sleep();

            serviceModule.bruteforce(ip);
            getLog().info("IP: {} - started bruteforce", ip);
            sleep();

            serviceModule.getRemoteLog(ip);
            return true;

        } catch (IpNotExistsException e) {
            getLog().info("Unable to exploit because Target IP '{}' is no longer valid", ip);

        } catch (Exception e) {
            getLog().error("There was an exploit error. IP: " + ip, e);
        } finally {
            clearSystemLog(ip);
        }
        return false;
    }

    /**
     * Zpracuje aktuální připravené tásky. Odstraní takové, které není možné prolomit a zjistí čas potřebný pro
     * dokončení všech bruteforce.
     */
    private long processPreparedTasks(WorkData workData, boolean reloadTasks) {
        if (reloadTasks) {
            workData.taskResponse = taskModule.getTasks();
        }
        long maxWaitTime = Integer.MIN_VALUE;

        for (var ip : workData.taskResponse.getBrutedIps()) {
            var leftTime = Math.max(0, ip.getEndTime() - ip.getCurrentTime());

            if (leftTime > getConfig().getNetworkMaxWaitingBruteTime()) {
                getLog().info("IP: {} - bruteforce takes too long ({}). It will be removed", ip.getIp(), SharedUtils
                        .toTimeFormat(leftTime * 1000));
                workData.taskResponse = taskModule.abortBruteforce(ip.getBruteId());
                sleep();
            }
            if (leftTime > maxWaitTime) {
                maxWaitTime = leftTime;
            }
        }
        return maxWaitTime;
    }

    /**
     * Vyčistí systémový log na cílovém systému.
     */
    private boolean clearSystemLog(String ip) {
        try {
            logModule.setRemoteLog(ip, getConfig().getMessageLog());
            getLog().info("IP: {} - the system log has been set", ip);
            return true;

        } catch (Exception e) {
            // nic
        }
        return false;
    }

    /**
     * Zahájí útok na banku.
     */
    private void processBrutedIps(WorkData workData, boolean reloadTasks, boolean realoadPlayerBank) {
        if (reloadTasks) {
            workData.taskResponse = taskModule.getTasks();
            sleep();
        }
        if (realoadPlayerBank) {
            workData.playerBankResponse = bankModule.getBank();
            sleep();
        }
        for (var ip : workData.taskResponse.getBrutedIps()) {
            var removeBrute = true;
            try {
                switch (BruteState.getbyState(ip.getResult())) {
                    case FAILED:
                        getLog().info("IP: {} - bruteforce failed. IP will be removed", ip.getIp());
                        workData.taskResponse = taskModule.removeBruteforce(ip.getBruteId());
                        sleep();
                        break;

                    case SUCCESS:
                        getLog().info("IP: {} - open the bank", ip.getIp());
                        var targetBank = bankModule.getRemoteBank(ip.getIp());
                        getLog().info("IP: {} - the bank has {} money", ip.getIp(), targetBank.getTotal());
                        sleep(1000);

                        for (var trans : targetBank.getTransactions()) {
                            databaseService.addTransaction(trans);
                        }
                        // pokud má smysl vykrást peníze z banky
                        if (workData.playerBankResponse.getTotal() < getConfig().getNetworkUserBankLimit() && targetBank
                                .getTotal() >= getConfig().getNetworkMinBankAmountForWithdraw()) {

                            var withdrawPercent = (getConfig().getNetworkWithdrawPercentAmount() > 100) ? 100. : Math
                                    .max(getConfig().getNetworkWithdrawPercentAmount(), 0);

                            // pokud procento peněz, které se mají vykrást bude 0, tak se bruteforce automatický
                            // odstraní
                            if (withdrawPercent > 0) {
                                MalwareKitResponse malwareResp = null;
                                var canWithdraw = true;

                                // zjistí, zda je možné vykrást banku
                                if (!getConfig().isNetworkWithdrawWithoutMalwares()) {
                                    malwareResp = malwareModule.getMalwareKit();
                                    if (malwareResp.getMalwares() < getConfig().getSafeMalwares()) {
                                        getLog().warn("IP: {} - malwares is not available. Bank money will not be stolen", ip
                                                .getIp());
                                        canWithdraw = false;
                                    }
                                }
                                if (canWithdraw) {
                                    var amount = (long) ((withdrawPercent / 100.) * targetBank.getTotal());
                                    var resp = bankModule.withdraw(ip.getIp(), amount);

                                    if (resp.getTransactionsCount() > 0) {
                                        // naše krádež by měla být jako první v seznamu
                                        var currTransaction = resp.getTransactions().get(0);

                                        // ověříme, zda první krádež je opravdu naše
                                        if (currTransaction.getToId()
                                                .equals(getShared().getUpdateResponse().getUid())) {
                                            getLog().info("IP: {} - {} money was transferred to your bank", ip
                                                    .getIp(), amount);

                                            // pokude náš IPSP je příliž nízký a je vidět naše IP, tak se jí pomocí
                                            // malware pokusíme skrýt
                                            if (Utils.isValidIp(currTransaction.getToIp())) {
                                                if (malwareResp == null) {
                                                    sleep();
                                                    malwareResp = malwareModule.getMalwareKit();
                                                }
                                                if (malwareResp.getMalwares() > 0 && malwareResp
                                                        .getMalwares() > getConfig().getSafeMalwares()) {

                                                    bankModule.useMalware(ip.getIp());
                                                    getLog().info("IP: {} - malware to hide IP was used. The remaining number of malwares: ", ip
                                                            .getIp(), malwareResp.getMalwares() - 1);
                                                } else {
                                                    getLog().warn("IP: {} - no malware to hide IP!", ip.getIp());
                                                }
                                            }
                                        }
                                        // cílová banka má hodně peněz, které by šlo ještě v budoucnu použít
                                        if (targetBank.getTotal() > getConfig().getNetworkKeepBruteforceByBankMoney()) {
                                            getLog().info("IP: {} - the bank contained more money than normal and brutal force would be left", ip
                                                    .getIp());
                                            removeBrute = false;
                                        } else {
                                            getLog().info("IP: {} - bruteforce will be removed", ip.getIp());
                                        }
                                    } else {
                                        getLog().warn("IP: {} - failed to transfer money from the bank. Bruteforce will be removed", ip
                                                .getIp());
                                    }
                                }
                            }
                        } else {
                            // banka má méně peněz, něž je minimální množství pro krádež, takže bruteforce ostraníme
                            if (targetBank.getTotal() < getConfig().getNetworkMinBankAmountForWithdraw()) {
                                getLog().info("IP: {} - the target bank has too low money and the bruteforce will be removed", ip
                                        .getIp());
                            }
                        }
                        // vyčistí logy a případně smaže bruteforce
                        clearSystemLog(ip.getIp());
                        if (removeBrute) {
                            sleep();
                            workData.taskResponse = taskModule.removeBruteforce(ip.getBruteId());
                        }
                        break;
                }
            } catch (ExploitException e) {
                // může nastat u starých bruteforce IP, který již expirovali nebo jejich IP není již platná
                getLog().error("IP: {} - insufficient permissions to access the system or invalid IP", ip.getIp());
                sleep();
                workData.taskResponse = taskModule.removeBruteforce(ip.getBruteId());
                sleep();

            } catch (Exception e) {
                clearSystemLog(ip.getIp());
                sleep();
            }
        }
    }

    private static final class WorkData {

        private int sdk;
        private int level;
        private int exploitsLeft;
        private final List<String> brutedIps = new ArrayList<>();
        private TaskResponse taskResponse;
        private BankResponse playerBankResponse;

        @Override
        public String toString() {
            return String.format("SDK: %s, Player level: %s, Bruted IPs: %s", sdk, level, brutedIps.size());
        }
    }
}
