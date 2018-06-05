package cz.ophite.mimic.vhackos.botnet.dto;

import cz.ophite.mimic.vhackos.botnet.api.net.response.TaskResponse;
import cz.ophite.mimic.vhackos.botnet.api.net.response.UpdateResponse;
import cz.ophite.mimic.vhackos.botnet.shared.utils.SharedUtils;

/**
 * Data, která jsou sdílená napříč všema službama.
 *
 * @author mimic
 */
public final class BotnetSharedData {

    private static final int MAX_TASKS = 10;
    private static final int MAX_VIP_TASKS = 12;

    private volatile UpdateResponse updateResponse;
    private volatile TaskResponse taskResponse;
    private volatile BotnetUpdateData updateData;

    private int maxTaskUpdates;

    public UpdateResponse getUpdateResponse() {
        return updateResponse;
    }

    public void setUpdateResponse(UpdateResponse updateResponse) {
        this.updateResponse = updateResponse;
        var vip = SharedUtils.toBoolean(updateResponse.getVip());
        maxTaskUpdates = vip ? MAX_VIP_TASKS : MAX_TASKS;
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

    public BotnetUpdateData getUpdateData() {
        return updateData;
    }

    public void setUpdateData(BotnetUpdateData updateData) {
        this.updateData = updateData;
    }
}
