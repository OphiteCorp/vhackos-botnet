package cz.ophite.mimic.vhackos.botnet.servicemodule;

import cz.ophite.mimic.vhackos.botnet.api.exception.IpNotExistsException;
import cz.ophite.mimic.vhackos.botnet.api.module.BankModule;
import cz.ophite.mimic.vhackos.botnet.api.module.LogModule;
import cz.ophite.mimic.vhackos.botnet.api.module.NetworkModule;
import cz.ophite.mimic.vhackos.botnet.api.module.RemoteModule;
import cz.ophite.mimic.vhackos.botnet.api.net.response.ExploitResponse;
import cz.ophite.mimic.vhackos.botnet.api.net.response.NetworkScanResponse;
import cz.ophite.mimic.vhackos.botnet.api.net.response.RemoteSystemResponse;
import cz.ophite.mimic.vhackos.botnet.db.service.DatabaseService;
import cz.ophite.mimic.vhackos.botnet.shared.injection.Autowired;
import cz.ophite.mimic.vhackos.botnet.shared.injection.Inject;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * Servisní modul pro práci s bankou.
 *
 * @author mimic
 */
@Inject
public final class ServiceModule {

    @Autowired
    private DatabaseService databaseService;

    @Autowired
    private BankModule bankModule;

    @Autowired
    private NetworkModule networkModule;

    @Autowired
    private RemoteModule remoteModule;

    @Autowired
    private LogModule logModule;

    /**
     * Proskenuje síť.
     */
    public NetworkScanResponse scan() {
        var resp = networkModule.scan();

        for (var ip : resp.getIps()) {
            databaseService.addScanIp(ip);
        }
        for (var ip : resp.getBrutedIps()) {
            databaseService.updateScanIp(ip.getIp(), ip.getUserName(), null);
        }
        return resp;
    }

    /**
     * Exploituje IP.
     */
    public ExploitResponse exploit(String ip) {
        try {
            var resp = networkModule.exploit(ip);

            for (var bruteIp : resp.getBrutedIps()) {
                databaseService.updateScanIp(bruteIp.getIp(), bruteIp.getUserName(), null);
            }
            return resp;

        } catch (IpNotExistsException e) {
            databaseService.invalidIp(ip);
            throw e;
        }
    }

    /**
     * Prolomí IP banky.
     */
    public void bruteforce(String ip) {
        try {
            bankModule.bruteforce(ip);

        } catch (IpNotExistsException e) {
            databaseService.invalidIp(ip);
            throw e;
        }
    }

    /**
     * Získá informace o systému.
     */
    public RemoteSystemResponse getSystemInfo(String ip) {
        var resp = remoteModule.getSystemInfo(ip);
        databaseService.updateScanIp(ip, resp.getUserName(), resp.getLevel());
        return resp;
    }

    /**
     * Získá vzdálený log.
     */
    public List<String> getRemoteLog(String ip) {
        var remoteLog = logModule.getRemoteLog(ip);
        databaseService.addLog(ip, StringUtils.join(remoteLog, '\n'));
        return remoteLog;
    }
}
