package cz.ophite.mimic.vhackos.botnet.api.opcode;

import cz.ophite.mimic.vhackos.botnet.api.opcode.base.Opcode;
import cz.ophite.mimic.vhackos.botnet.api.opcode.base.OpcodeTargetType;

/**
 * Získá všechny tásky uživatele.
 *
 * @author mimic
 */
public final class TaskOpcode extends Opcode {

    @Override
    public OpcodeTargetType getTarget() {
        return OpcodeTargetType.TASKS;
    }
}
