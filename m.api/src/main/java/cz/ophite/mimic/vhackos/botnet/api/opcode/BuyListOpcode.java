package cz.ophite.mimic.vhackos.botnet.api.opcode;

import cz.ophite.mimic.vhackos.botnet.api.opcode.base.Opcode;
import cz.ophite.mimic.vhackos.botnet.api.opcode.base.OpcodeTargetType;

/**
 * Získá seznam položek ke koupi.
 *
 * @author mimic
 */
public final class BuyListOpcode extends Opcode {

    private static final String PARAM_INFO = "info";

    public BuyListOpcode() {
        addParam(PARAM_INFO, "1");
    }

    @Override
    public OpcodeTargetType getTarget() {
        return OpcodeTargetType.BUY;
    }
}
