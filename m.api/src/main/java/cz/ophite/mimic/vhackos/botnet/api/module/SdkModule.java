package cz.ophite.mimic.vhackos.botnet.api.module;

import cz.ophite.mimic.vhackos.botnet.api.IBotnet;
import cz.ophite.mimic.vhackos.botnet.api.module.base.Module;
import cz.ophite.mimic.vhackos.botnet.api.module.base.ModuleHelper;
import cz.ophite.mimic.vhackos.botnet.api.net.response.SdkResponse;
import cz.ophite.mimic.vhackos.botnet.api.opcode.SdkBuyOpcode;
import cz.ophite.mimic.vhackos.botnet.api.opcode.SdkOpcode;
import cz.ophite.mimic.vhackos.botnet.api.opcode.base.Opcode;
import cz.ophite.mimic.vhackos.botnet.shared.injection.Inject;

/**
 * Správa SDK pro exploit.
 *
 * @author mimic
 */
@Inject
public final class SdkModule extends Module {

    protected SdkModule(IBotnet botnet) {
        super(botnet);
    }

    /**
     * Získá informace o SDK.
     */
    public synchronized SdkResponse getSdk() {
        var opcode = new SdkOpcode();
        return createSdkResponse(opcode);
    }

    /**
     * Koupí další SDK pro exploit.
     */
    public synchronized SdkResponse buySdk() {
        var opcode = new SdkBuyOpcode();
        return createSdkResponse(opcode);
    }

    private SdkResponse createSdkResponse(Opcode opcode) {
        var response = sendRequest(opcode);
        var dto = new SdkResponse();

        ModuleHelper.checkResponseIntegrity(response.keySet(), SdkResponse.class);
        ModuleHelper.setField(response, dto, SdkResponse.P_APPLIED);
        ModuleHelper.setField(response, dto, SdkResponse.P_SDK);
        ModuleHelper.setField(response, dto, SdkResponse.P_EXPLOITS);
        ModuleHelper.setField(response, dto, SdkResponse.P_NEXT_EXPLOIT);

        return dto;
    }
}
