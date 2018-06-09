package cz.ophite.mimic.vhackos.botnet.service;

import cz.ophite.mimic.vhackos.botnet.Botnet;
import cz.ophite.mimic.vhackos.botnet.api.exception.BotnetException;
import cz.ophite.mimic.vhackos.botnet.api.exception.InvalidAccessTokenException;
import cz.ophite.mimic.vhackos.botnet.api.exception.ServerBusyException;
import cz.ophite.mimic.vhackos.botnet.api.module.TaskModule;
import cz.ophite.mimic.vhackos.botnet.api.net.response.NetworkScanResponse;
import cz.ophite.mimic.vhackos.botnet.db.service.DatabaseService;
import cz.ophite.mimic.vhackos.botnet.service.base.EndpointService;
import cz.ophite.mimic.vhackos.botnet.service.base.IService;
import cz.ophite.mimic.vhackos.botnet.service.base.Service;
import cz.ophite.mimic.vhackos.botnet.servicemodule.ServiceModule;
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
    private TaskModule taskModule;

    @Autowired
    private ServiceModule serviceModule;

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
        List<String> dbIps = null;

        if (enableBrute && getConfig().isNetworkScanPreferDatabase()) {
            dbIps = databaseService.getScannedIpsWithoutUser(20);
        }
        if (dbIps != null && !dbIps.isEmpty()) {
            getLog().info("{} IPs were obtained from the database and they do not have the assigned user name", dbIps
                    .size());
            brutedIps = new ArrayList(20);
            counter.incrementAndGet();
            processDatabaseIps(dbIps);
        } else {
            if (enableBrute && brutedIps == null) {
                brutedIps = prepareListOfBrutedIps();
                sleep();
            }
            counter.incrementAndGet();
            processNetwork(enableBrute);
        }
    }

    private void processDatabaseIps(List<String> ips) {
        for (var ip : ips) {
            getLog().info("Getting a user from IP: {}", ip);
            bruteIp(ip);
            sleep();
        }
        processBrutedIps();
        brutedIps.clear();
        brutedIps = null;

        var pause = getConfig().getNetworkScanPause();
        getLog().info("Done. I'm waiting: {}", SharedUtils.toTimeFormat(pause));
        sleep(pause);
    }

    private void processNetwork(boolean enableBrute) {
        var resp = scan();

        for (var ip : resp.getIps()) {
            var scannedIp = databaseService.addScanIp(ip);

            if (enableBrute) {
                bruteIp(scannedIp.getIp());
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
            getLog().info("Done. Waiting: {}", SharedUtils.toTimeFormat(pause));
            sleep(pause);
        } else {
            if (isRunningAsync()) {
                getLog().info("Counter: {}/{}. Next scan will be in: {}", counter, scansCountBeforePause, SharedUtils
                        .toTimeFormat(getTimeout()));
            }
        }
    }

    private NetworkScanResponse scan() {
        try {
            return serviceModule.scan();

        } catch (InvalidAccessTokenException e) {
            getLog().warn("An error occurred while scanning the network. The service will automatically restart in 5 seconds", e);
            stop();
            SharedUtils.runAsyncProcess(() -> {
                sleep(5000);
                start();
            });
            throw e;
        }
    }

    private void bruteIp(String ip) {
        if (brutedIps == null && brutedIps.contains(ip)) {
            return;
        }
        try {
            if (recursiveBruteIp(ip, 1, 3)) {
                brutedIps.add(ip);
            } else {
                getLog().warn("It was not possible to break IP because the server was too busy. IP '{}' will be skipped", ip);
            }
        } catch (BotnetException e) {
            getLog().warn("There was an error. It will be skipped. Message: {}", e.getMessage());
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
            serviceModule.bruteforce(ip);

        } catch (ServerBusyException e) {
            getLog().warn("The server is busy. Current attempt: {}", attempts);
            sleep(5000);
            recursiveBruteIp(ip, ++attempts, max);
        }
        return true;
    }
}
