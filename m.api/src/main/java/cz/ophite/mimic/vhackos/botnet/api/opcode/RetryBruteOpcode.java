package cz.ophite.mimic.vhackos.botnet.api.opcode;

import cz.ophite.mimic.vhackos.botnet.api.opcode.base.Opcode;
import cz.ophite.mimic.vhackos.botnet.api.opcode.base.OpcodeTargetType;

/**
 * Znovu se pokusí prolomit banku uživatele.
 *
 * @author mimic
 */
public final class RetryBruteOpcode extends Opcode {

    private static final String PARAM_TARGET_IP = "target";

    /**
     * Cílová IP.
     */
    public void setTargetIp(String targetIp) {
        addParam(PARAM_TARGET_IP, targetIp);
    }

    @Override
    public OpcodeTargetType getTarget() {
        return OpcodeTargetType.TASKS;
    }

    @Override
    public String getOpcodeValue() {
        return "10005";
    }
}
