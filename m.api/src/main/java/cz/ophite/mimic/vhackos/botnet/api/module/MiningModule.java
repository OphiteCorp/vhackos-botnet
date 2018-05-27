package cz.ophite.mimic.vhackos.botnet.api.module;

import cz.ophite.mimic.vhackos.botnet.api.IBotnet;
import cz.ophite.mimic.vhackos.botnet.api.module.base.Module;
import cz.ophite.mimic.vhackos.botnet.api.module.base.ModuleHelper;
import cz.ophite.mimic.vhackos.botnet.api.net.response.MiningResponse;
import cz.ophite.mimic.vhackos.botnet.api.opcode.MiningBuyGpuOpcode;
import cz.ophite.mimic.vhackos.botnet.api.opcode.MiningCollectOpcode;
import cz.ophite.mimic.vhackos.botnet.api.opcode.MiningOpcode;
import cz.ophite.mimic.vhackos.botnet.api.opcode.MiningStartOpcode;
import cz.ophite.mimic.vhackos.botnet.api.opcode.base.Opcode;
import cz.ophite.mimic.vhackos.botnet.shared.injection.Inject;

/**
 * Správa netcoin mineru.
 *
 * @author mimic
 */
@Inject
public final class MiningModule extends Module {

    protected MiningModule(IBotnet botnet) {
        super(botnet);
    }

    /**
     * Získá informace o netcoin mineru.
     */
    public synchronized MiningResponse getMining() {
        var opcode = new MiningOpcode();
        return createMiningResponse(opcode);
    }

    /**
     * Vybere netcoins z mineru.
     */
    public synchronized MiningResponse collect() {
        var opcode = new MiningCollectOpcode();
        return createMiningResponse(opcode);
    }

    /**
     * Zakoupí další GPU do mineru.
     */
    public synchronized MiningResponse buyGpu() {
        var opcode = new MiningBuyGpuOpcode();
        return createMiningResponse(opcode);
    }

    /**
     * Spustí netcoin miner.
     */
    public synchronized MiningResponse start() {
        var opcode = new MiningStartOpcode();
        return createMiningResponse(opcode);
    }

    private MiningResponse createMiningResponse(Opcode opcode) {
        var response = sendRequest(opcode);
        var dto = new MiningResponse();
        ModuleHelper.checkResponseIntegrity(response.keySet(), MiningResponse.class);
        ModuleHelper.setField(response, dto, MiningResponse.P_UPGRADED);
        ModuleHelper.setField(response, dto, MiningResponse.P_RUNNING);
        ModuleHelper.setField(response, dto, MiningResponse.P_APPLIED);
        ModuleHelper.setField(response, dto, MiningResponse.P_CLAIMED);
        ModuleHelper.setField(response, dto, MiningResponse.P_STARTED);
        ModuleHelper.setField(response, dto, MiningResponse.P_MINED);
        ModuleHelper.setField(response, dto, MiningResponse.P_NETCOINS);
        ModuleHelper.setField(response, dto, MiningResponse.P_GPU_COUNT);
        ModuleHelper.setField(response, dto, MiningResponse.P_UPGRADABLE);
        ModuleHelper.setField(response, dto, MiningResponse.P_MINING_SPEED);
        ModuleHelper.setField(response, dto, MiningResponse.P_NEW_GPU_COSTS);
        ModuleHelper.setField(response, dto, MiningResponse.P_IS);
        ModuleHelper.setField(response, dto, MiningResponse.P_LEFT);
        ModuleHelper.setField(response, dto, MiningResponse.P_NEED);
        ModuleHelper.setField(response, dto, MiningResponse.P_WILL_EARN);
        return dto;
    }
}
