package cz.ophite.mimic.vhackos.botnet.api.opcode;

import cz.ophite.mimic.vhackos.botnet.api.opcode.base.Opcode;
import cz.ophite.mimic.vhackos.botnet.api.opcode.base.OpcodeTargetType;

/**
 * Přidá FW node do serveru.
 *
 * @author mimic
 */
public final class ServerAddFwNodeOpcode extends Opcode {

    @Override
    public OpcodeTargetType getTarget() {
        return OpcodeTargetType.SERVER;
    }

    @Override
    public String getOpcodeValue() {
        return "200";
    }
}
