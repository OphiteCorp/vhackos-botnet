package cz.ophite.mimic.vhackos.botnet.api.opcode;

import cz.ophite.mimic.vhackos.botnet.api.opcode.base.Opcode;
import cz.ophite.mimic.vhackos.botnet.api.opcode.base.OpcodeTargetType;

/**
 * Odešle crew zprávu.
 *
 * @author mimic
 */
public final class CrewSendMessageOpcode extends Opcode {

    private static final String PARAM_MESSAGE = "message";

    /**
     * Zpráva.
     */
    public void setMessage(String message) {
        addParam(PARAM_MESSAGE, message);
    }

    @Override
    public OpcodeTargetType getTarget() {
        return OpcodeTargetType.CREW;
    }

    @Override
    public String getOpcodeValue() {
        return "1000";
    }
}
