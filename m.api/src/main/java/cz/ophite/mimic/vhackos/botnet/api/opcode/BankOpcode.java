package cz.ophite.mimic.vhackos.botnet.api.opcode;

import cz.ophite.mimic.vhackos.botnet.api.opcode.base.Opcode;
import cz.ophite.mimic.vhackos.botnet.api.opcode.base.OpcodeTargetType;

/**
 * Získá informace z vlastní banky.
 *
 * @author mimic
 */
public final class BankOpcode extends Opcode {

    @Override
    public OpcodeTargetType getTarget() {
        return OpcodeTargetType.BANKING;
    }

    @Override
    public String getOpcodeValue() {
        return "100";
    }
}
