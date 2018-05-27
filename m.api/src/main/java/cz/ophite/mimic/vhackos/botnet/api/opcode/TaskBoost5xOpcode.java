package cz.ophite.mimic.vhackos.botnet.api.opcode;

import cz.ophite.mimic.vhackos.botnet.api.opcode.base.Opcode;
import cz.ophite.mimic.vhackos.botnet.api.opcode.base.OpcodeTargetType;

/**
 * Použije 5x boost na tásk.
 *
 * @author mimic
 */
public final class TaskBoost5xOpcode extends Opcode {

    private static final String PARAM_UPDATE_ID = "updateid";

    /**
     * Task ID.
     */
    public void setTaskId(int taskId) {
        addParam(PARAM_UPDATE_ID, String.valueOf(taskId));
    }

    @Override
    public OpcodeTargetType getTarget() {
        return OpcodeTargetType.TASKS;
    }

    @Override
    public String getOpcodeValue() {
        return "8888";
    }
}
