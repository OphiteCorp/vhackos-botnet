package cz.ophite.mimic.vhackos.botnet.api.opcode;

import cz.ophite.mimic.vhackos.botnet.api.opcode.base.Opcode;
import cz.ophite.mimic.vhackos.botnet.api.opcode.base.OpcodeTargetType;
import cz.ophite.mimic.vhackos.botnet.shared.dto.AppStoreType;

/**
 * Koupí aplikaci v obchodu.
 *
 * @author mimic
 */
public final class BuyAppOpcode extends Opcode {

    private static final String PARAM_APP_CODE = "appcode";

    /**
     * Typ aplikace.
     */
    public void setApp(AppStoreType app) {
        addParam(PARAM_APP_CODE, String.valueOf(app.getId()));
    }

    @Override
    public OpcodeTargetType getTarget() {
        return OpcodeTargetType.STORE;
    }

    @Override
    public String getOpcodeValue() {
        return "100";
    }
}
