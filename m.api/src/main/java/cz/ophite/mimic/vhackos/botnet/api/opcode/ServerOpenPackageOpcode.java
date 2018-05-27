package cz.ophite.mimic.vhackos.botnet.api.opcode;

import cz.ophite.mimic.vhackos.botnet.api.opcode.base.Opcode;
import cz.ophite.mimic.vhackos.botnet.api.opcode.base.OpcodeTargetType;

/**
 * Otevře jeden balíček na serveru.
 *
 * @author mimic
 */
public class ServerOpenPackageOpcode extends Opcode {

    @Override
    public OpcodeTargetType getTarget() {
        return OpcodeTargetType.SERVER;
    }

    @Override
    public String getOpcodeValue() {
        return "1000";
    }
}
