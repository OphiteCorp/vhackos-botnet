package cz.ophite.mimic.vhackos.botnet.api.opcode;

import cz.ophite.mimic.vhackos.botnet.api.opcode.base.Opcode;
import cz.ophite.mimic.vhackos.botnet.api.opcode.base.OpcodeTargetType;

/**
 * Nastaví do vlastního logu zprávu.
 *
 * @author mimic
 */
public final class UpdateLogOpcode extends Opcode {

    private static final String PARAM_LOG = "log";

    /**
     * Nová záznam v logu.
     */
    public void setLog(String log) {
        addParam(PARAM_LOG, log);
    }

    @Override
    public OpcodeTargetType getTarget() {
        return OpcodeTargetType.LOG;
    }

    @Override
    public String getOpcodeValue() {
        return "100";
    }
}
