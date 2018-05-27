package cz.ophite.mimic.vhackos.botnet.api.opcode;

import cz.ophite.mimic.vhackos.botnet.api.opcode.base.Opcode;
import cz.ophite.mimic.vhackos.botnet.api.opcode.base.OpcodeTargetType;

/**
 * Dokončí tásky pomocí netcoins.
 *
 * @author mimic
 */
public final class TasksFinishForNetCoinsOpcode extends Opcode {

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
        return "500";
    }
}
