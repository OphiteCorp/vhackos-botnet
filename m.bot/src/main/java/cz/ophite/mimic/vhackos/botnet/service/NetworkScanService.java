package cz.ophite.mimic.vhackos.botnet.service;

import cz.ophite.mimic.vhackos.botnet.Botnet;
import cz.ophite.mimic.vhackos.botnet.api.exception.ServerBusyException;
import cz.ophite.mimic.vhackos.botnet.api.module.BankModule;
import cz.ophite.mimic.vhackos.botnet.api.module.NetworkModule;
import cz.ophite.mimic.vhackos.botnet.api.module.TaskModule;
import cz.ophite.mimic.vhackos.botnet.db.entity.ScannedIpEntity;
import cz.ophite.mimic.vhackos.botnet.db.service.DatabaseService;
import cz.ophite.mimic.vhackos.botnet.service.base.EndpointService;
import cz.ophite.mimic.vhackos.botnet.service.base.IService;
import cz.ophite.mimic.vhackos.botnet.service.base.Service;
import cz.ophite.mimic.vhackos.botnet.shared.injection.Autowired;
import cz.ophite.mimic.vhackos.botnet.shared.injection.Inject;
import cz.ophite.mimic.vhackos.botnet.shared.utils.SharedUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Služba, která pouze skenuje síť a ukládá data do DB.
 *
 * @author mimic
 */
@Inject
@EndpointService(IService.SERVICE_NETWORK_SCAN)
public final class NetworkScanService extends Service {

    @Autowired
    private DatabaseService databaseService;

    @Autowired
    private NetworkModule networkModule;

    @Autowired
    private BankModule bankModule;

    @Autowired
    private TaskModule taskModule;

    private volatile Integer scansCountBeforePause;
    private volatile AtomicInteger counter;
    private List<String> brutedIps;

    protected NetworkScanService(Botnet botnet) {
        super(botnet);
    }

    @Override
    public String getDescription() {
        return "Searchs the network and saves data into the database";
    }

    @Override
    protected void initialize() {
        setTimeout(getConfig().getNetworkScanTimeout());

        if (scansCountBeforePause == null) {
            scansCountBeforePause = getConfig().getNetworkScanCountBeforePause();
            counter = new AtomicInteger(0);
        }
    }

    @Override
    protected void onStopped() {
        scansCountBeforePause = null;
    }

    @Override
    protected void execute() {
        if (!databaseService.isDatabaseConnected()) {
            getLog().warn("There is no database connection available. This service requires it");
            return;
        }
        var enableBrute = getConfig().isNetworkScanAllowBrute();

        if (enableBrute && brutedIps == null) {
            brutedIps = prepareListOfBrutedIps();
            sleep();
        }
        counter.incrementAndGet();
        var resp = networkModule.scan();

        for (var ip : resp.getIps()) {
            getLog().info("Adding a new IP to the database: {}", ip.getIp());
            var scannedIp = databaseService.addScanIp(ip);

            if (enableBrute) {
                bruteIp(scannedIp);
            }
        }
        if (counter.get() >= scansCountBeforePause) {
            var pause = getConfig().getNetworkScanPause();
            getLog().info("Counter: {}/{}. There will be a break in length: {}", counter, scansCountBeforePause, SharedUtils
                    .toTimeFormat(getTimeout() + pause));
            scansCountBeforePause = null;

            if (enableBrute) {
                processBrutedIps();
                brutedIps.clear();
                brutedIps = null;
            }
            getLog().info("Done. I'm waiting: {}", SharedUtils.toTimeFormat(pause));
            sleep(pause);
        } else {
            if (isRunningAsync()) {
                getLog().info("Counter: {}/{}. Next scan will be in: {}", counter, scansCountBeforePause, SharedUtils
                        .toTimeFormat(getTimeout()));
            }
        }
    }

    private void bruteIp(ScannedIpEntity scannedIp) {
        if (brutedIps == null && brutedIps.contains(scannedIp.getIp())) {
            return;
        }
        if (recursiveBruteIp(scannedIp.getIp(), 1, 3)) {
            brutedIps.add(scannedIp.getIp());
        } else {
            getLog().warn("It was not possible to break IP because the server was too busy. IP '{}' will be skipped", scannedIp
                    .getIp());
        }
    }

    private void processBrutedIps() {
        sleep();
        var resp = taskModule.getTasks();

        for (var brute : resp.getBrutedIps()) {
            getLog().info("Updating an existing IP: {}", brute.getIp());
            databaseService.updateScanIp(brute.getIp(), brute.getUserName(), null);
            taskModule.removeBruteforce(brute.getBruteId());
        }
        getLog().info("Updating all IPs has been completed");
    }

    private List<String> prepareListOfBrutedIps() {
        var resp = taskModule.getTasks();
        getShared().setTaskResponse(resp);

        var list = new ArrayList<String>(resp.getBrutedIps().size());

        for (var brute : resp.getBrutedIps()) {
            list.add(brute.getIp());
        }
        return list;
    }

    private boolean recursiveBruteIp(String ip, int attempts, int max) {
        if (attempts > max) {
            return false;
        }
        try {
            bankModule.bruteforce(ip);
        } catch (ServerBusyException e) {
            getLog().warn("The server is busy. Current attempt: {}", attempts);
            sleep(5000);
            recursiveBruteIp(ip, ++attempts, max);
        }
        return true;
    }
}
