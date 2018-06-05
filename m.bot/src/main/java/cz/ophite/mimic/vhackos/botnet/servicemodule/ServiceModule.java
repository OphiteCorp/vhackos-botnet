package cz.ophite.mimic.vhackos.botnet.servicemodule;

import cz.ophite.mimic.vhackos.botnet.api.exception.BotnetException;
import cz.ophite.mimic.vhackos.botnet.api.exception.IpNotExistsException;
import cz.ophite.mimic.vhackos.botnet.api.module.BankModule;
import cz.ophite.mimic.vhackos.botnet.api.module.NetworkModule;
import cz.ophite.mimic.vhackos.botnet.api.module.RemoteModule;
import cz.ophite.mimic.vhackos.botnet.api.net.response.ExploitResponse;
import cz.ophite.mimic.vhackos.botnet.api.net.response.RemoteSystemResponse;
import cz.ophite.mimic.vhackos.botnet.db.service.DatabaseService;
import cz.ophite.mimic.vhackos.botnet.shared.injection.Autowired;
import cz.ophite.mimic.vhackos.botnet.shared.injection.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Servisní modul pro práci s bankou.
 *
 * @author mimic
 */
@Inject
public final class ServiceModule {

    private static final Logger LOG = LoggerFactory.getLogger(ServiceModule.class);

    @Autowired
    private DatabaseService databaseService;

    @Autowired
    private BankModule bankModule;

    @Autowired
    private NetworkModule networkModule;

    @Autowired
    private RemoteModule remoteModule;

    /**
     * Exploituje IP.
     */
    public ExploitResponse exploit(String ip) {
        try {
            return networkModule.exploit(ip);

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

        } catch (BotnetException e) {
            throw e;
        }
    }

    /**
     * Získá informace o systému.
     */
    public RemoteSystemResponse getSystemInfo(String ip) {
        var resp = remoteModule.getSystemInfo(ip);
        LOG.info("Updating an existing IP: {}", ip);
        databaseService.updateScanIp(ip, resp.getUserName(), resp.getLevel());
        return resp;
    }
}
