package cz.ophite.mimic.vhackos.botnet.service;

import cz.ophite.mimic.vhackos.botnet.Botnet;
import cz.ophite.mimic.vhackos.botnet.api.module.MiningModule;
import cz.ophite.mimic.vhackos.botnet.api.net.response.MiningResponse;
import cz.ophite.mimic.vhackos.botnet.service.base.EndpointService;
import cz.ophite.mimic.vhackos.botnet.service.base.IService;
import cz.ophite.mimic.vhackos.botnet.service.base.Service;
import cz.ophite.mimic.vhackos.botnet.shared.dto.MinerState;
import cz.ophite.mimic.vhackos.botnet.shared.injection.Autowired;
import cz.ophite.mimic.vhackos.botnet.shared.injection.Inject;
import cz.ophite.mimic.vhackos.botnet.shared.utils.SharedUtils;

/**
 * Služba, která kontroluje a ovládá miner.
 *
 * @author mimic
 */
@Inject
@EndpointService(IService.SERVICE_MINER)
public final class MinerService extends Service {

    private static final int MAX_GPU_COUNT = 5;

    @Autowired
    private MiningModule miningModule;

    protected MinerService(Botnet botnet) {
        super(botnet);
    }

    @Override
    public String getDescription() {
        return "Controls and manages netcoins mining";
    }

    @Override
    protected void initialize() {
        setTimeout(getConfig().getMinerTimeout());
    }

    @Override
    protected void execute() {
        if (!SharedUtils.toBoolean(getShared().getUpdateResponse().getMiner())) {
            getLog().info("Miner is not available. Next check will be in: {}", SharedUtils.toTimeFormat(getTimeout()));
            return;
        }
        var resp = miningModule.getMining();
        var state = MinerState.getByCode(resp.getRunning());
        var currentNetcoins = resp.getNetCoins();

        getLog().info("{} -> Netcoins: {}, GPUs: {}", state.getAlias(), resp.getNetCoins(), resp.getGpuCount());

        switch (state) {
            case STOPPED:
                tryBuyGpu(resp);
                break;

            case FINISHED:
                resp = miningModule.collect();
                getLog().info("{} netcoins were mined", resp.getNetCoins() - currentNetcoins);
                sleep();
                tryBuyGpu(resp);
                break;

            case RUNNING:
                getLog().info("Will finish in: {}. Next check will be in: {}", SharedUtils
                        .toTimeFormat(resp.getLeft() * 1000), SharedUtils.toTimeFormat(getTimeout()));
                return;
        }
        miningModule.start();
        getLog().info("Miner was started. Next check will be in: {}", SharedUtils.toTimeFormat(getTimeout()));
    }

    private MiningResponse tryBuyGpu(MiningResponse resp) {
        if (resp.getGpuCount() < MAX_GPU_COUNT && resp.getNetCoins() >= resp.getNewGpuCosts()) {
            getLog().info("Buying {} GPU for {} netcoins", resp.getGpuCount() + 1, resp.getNewGpuCosts());
            resp = miningModule.buyGpu();
            getLog().info("Another GPU was bought. You have {} netcoins left", resp.getNetCoins());
            sleep();
        }
        return resp;
    }
}
