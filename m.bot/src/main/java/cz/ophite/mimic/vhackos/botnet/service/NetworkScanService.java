package cz.ophite.mimic.vhackos.botnet.service;

import cz.ophite.mimic.vhackos.botnet.Botnet;
import cz.ophite.mimic.vhackos.botnet.api.module.NetworkModule;
import cz.ophite.mimic.vhackos.botnet.db.service.DatabaseService;
import cz.ophite.mimic.vhackos.botnet.service.base.EndpointService;
import cz.ophite.mimic.vhackos.botnet.service.base.IService;
import cz.ophite.mimic.vhackos.botnet.service.base.Service;
import cz.ophite.mimic.vhackos.botnet.shared.injection.Autowired;
import cz.ophite.mimic.vhackos.botnet.shared.injection.Inject;
import cz.ophite.mimic.vhackos.botnet.shared.utils.SharedUtils;

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

    private volatile Integer scansCountBeforePause;
    private volatile AtomicInteger counter;

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
        counter.incrementAndGet();
        var resp = networkModule.scan();

        for (var ip : resp.getIps()) {
            databaseService.addScanIp(ip);
        }

        if (counter.get() >= scansCountBeforePause) {
            var pause = getConfig().getNetworkScanPause();
            getLog().info("Counter: {}/{}. There will be a break in length: {}", counter, scansCountBeforePause, SharedUtils
                    .toTimeFormat(getTimeout() + pause));
            scansCountBeforePause = null;
            sleep(pause);
        } else {
            if (isRunningAsync()) {
                getLog().info("Counter: {}/{}. Next scan will be in: {}", counter, scansCountBeforePause, SharedUtils
                        .toTimeFormat(getTimeout()));
            }
        }
    }
}
