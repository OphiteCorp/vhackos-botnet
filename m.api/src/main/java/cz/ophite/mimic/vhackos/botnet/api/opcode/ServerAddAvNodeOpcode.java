package cz.ophite.mimic.vhackos.botnet.api.opcode;

import cz.ophite.mimic.vhackos.botnet.api.opcode.base.Opcode;
import cz.ophite.mimic.vhackos.botnet.api.opcode.base.OpcodeTargetType;

/**
 * Přidá AV node do serveru.
 *
 * @author mimic
 */
public final class ServerAddAvNodeOpcode extends Opcode {

    @Override
    public OpcodeTargetType getTarget() {
        return OpcodeTargetType.SERVER;
    }

    @Override
    public String getOpcodeValue() {
        return "100";
    }
}
