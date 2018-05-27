package cz.ophite.mimic.vhackos.botnet.api.opcode;

import cz.ophite.mimic.vhackos.botnet.api.opcode.base.Opcode;
import cz.ophite.mimic.vhackos.botnet.api.opcode.base.OpcodeTargetType;

/**
 * Odebere bruteforce z tásků.
 *
 * @author mimic
 */
public final class RemoveBruteOpcode extends Opcode {

    private static final String PARAM_UPDATE_ID = "updateid";

    /**
     * Bruteforce ID.
     */
    public void setBruteforceId(int bruteId) {
        addParam(PARAM_UPDATE_ID, String.valueOf(bruteId));
    }

    @Override
    public OpcodeTargetType getTarget() {
        return OpcodeTargetType.TASKS;
    }

    @Override
    public String getOpcodeValue() {
        return "10000";
    }
}
