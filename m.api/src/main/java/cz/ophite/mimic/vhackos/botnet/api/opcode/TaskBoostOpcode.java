package cz.ophite.mimic.vhackos.botnet.api.opcode;

import cz.ophite.mimic.vhackos.botnet.api.opcode.base.Opcode;
import cz.ophite.mimic.vhackos.botnet.api.opcode.base.OpcodeTargetType;

/**
 * Použije boost na tásk.
 *
 * @author mimic
 */
public final class TaskBoostOpcode extends Opcode {

    private static final String PARAM_UPDATE_ID = "updateid";

    /**
     * Task ID.
     */
    public void setTaskId(long taskId) {
        addParam(PARAM_UPDATE_ID, String.valueOf(taskId));
    }

    @Override
    public OpcodeTargetType getTarget() {
        return OpcodeTargetType.TASKS;
    }

    @Override
    public String getOpcodeValue() {
        return "888";
    }
}
