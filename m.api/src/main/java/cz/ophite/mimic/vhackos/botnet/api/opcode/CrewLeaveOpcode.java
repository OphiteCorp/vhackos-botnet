package cz.ophite.mimic.vhackos.botnet.api.opcode;

import cz.ophite.mimic.vhackos.botnet.api.opcode.base.Opcode;
import cz.ophite.mimic.vhackos.botnet.api.opcode.base.OpcodeTargetType;

/**
 * Opustí crew.
 *
 * @author mimic
 */
public final class CrewLeaveOpcode extends Opcode {

    @Override
    public OpcodeTargetType getTarget() {
        return OpcodeTargetType.CREW;
    }

    @Override
    public String getOpcodeValue() {
        return "3000";
    }
}
