package cz.ophite.mimic.vhackos.botnet.api.opcode;

import cz.ophite.mimic.vhackos.botnet.api.opcode.base.Opcode;
import cz.ophite.mimic.vhackos.botnet.api.opcode.base.OpcodeTargetType;

/**
 * Upraví vzdálený log pro IP.
 *
 * @author mimic
 */
public final class UpdateRemoteLogOpcode extends Opcode {

    private static final String PARAM_TARGET = "target";
    private static final String PARAM_LOG = "log";

    /**
     * Cílová IP.
     */
    public void setTargetIp(String ip) {
        addParam(PARAM_TARGET, ip);
    }

    /**
     * Záznam v logu.
     */
    public void setLog(String log) {
        addParam(PARAM_LOG, log);
    }

    @Override
    public OpcodeTargetType getTarget() {
        return OpcodeTargetType.REMOTE_LOG;
    }

    @Override
    public String getOpcodeValue() {
        return "100";
    }
}
