package cz.ophite.mimic.vhackos.botnet.dto;

import cz.ophite.mimic.vhackos.botnet.api.net.response.TaskResponse;
import cz.ophite.mimic.vhackos.botnet.api.net.response.UpdateResponse;

/**
 * Data, která jsou sdílená napříč všema službama.
 *
 * @author mimic
 */
public final class BotnetSharedData {

    private UpdateResponse updateResponse;
    private TaskResponse taskResponse;

    private int maxTaskUpdates;

    public UpdateResponse getUpdateResponse() {
        return updateResponse;
    }

    public void setUpdateResponse(UpdateResponse updateResponse) {
        this.updateResponse = updateResponse;
    }

    public TaskResponse getTaskResponse() {
        return taskResponse;
    }

    public void setTaskResponse(TaskResponse taskResponse) {
        this.taskResponse = taskResponse;
    }

    public int getMaxTaskUpdates() {
        return maxTaskUpdates;
    }

    public void setMaxTaskUpdates(int maxTaskUpdates) {
        this.maxTaskUpdates = maxTaskUpdates;
    }
}
