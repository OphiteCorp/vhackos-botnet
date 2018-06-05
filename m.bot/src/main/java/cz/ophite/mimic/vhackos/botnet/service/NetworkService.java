package cz.ophite.mimic.vhackos.botnet.service;

import cz.ophite.mimic.vhackos.botnet.Botnet;
import cz.ophite.mimic.vhackos.botnet.api.module.BankModule;
import cz.ophite.mimic.vhackos.botnet.api.module.LogModule;
import cz.ophite.mimic.vhackos.botnet.api.module.NetworkModule;
import cz.ophite.mimic.vhackos.botnet.api.module.RemoteModule;
import cz.ophite.mimic.vhackos.botnet.db.service.DatabaseService;
import cz.ophite.mimic.vhackos.botnet.service.base.EndpointService;
import cz.ophite.mimic.vhackos.botnet.service.base.IService;
import cz.ophite.mimic.vhackos.botnet.service.base.Service;
import cz.ophite.mimic.vhackos.botnet.servicemodule.ServiceModule;
import cz.ophite.mimic.vhackos.botnet.shared.injection.Autowired;
import cz.ophite.mimic.vhackos.botnet.shared.injection.Inject;

/**
 * Služba pro správu sítě - skenování, prolamování banky, krádež peněz a čištění logů apod.
 *
 * @author mimic
 */
@Inject
@EndpointService(IService.SERVICE_NETWORK)
public final class NetworkService extends Service {

    @Autowired
    private DatabaseService databaseService;

    @Autowired
    private NetworkModule networkModule;

    @Autowired
    private BankModule bankModule;

    @Autowired
    private RemoteModule remoteModule;

    @Autowired
    private LogModule logModule;

    @Autowired
    private ServiceModule serviceModule;

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
        getLog().warn("Implementation of this service is not yet complete. Please turn it off in the configuration file");
    }
}
