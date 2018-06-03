package cz.ophite.mimic.vhackos.botnet.api.module;

import cz.ophite.mimic.vhackos.botnet.api.IBotnet;
import cz.ophite.mimic.vhackos.botnet.api.module.base.Module;
import cz.ophite.mimic.vhackos.botnet.api.module.base.ModuleHelper;
import cz.ophite.mimic.vhackos.botnet.api.net.response.TaskResponse;
import cz.ophite.mimic.vhackos.botnet.api.opcode.*;
import cz.ophite.mimic.vhackos.botnet.api.opcode.base.Opcode;
import cz.ophite.mimic.vhackos.botnet.shared.injection.Inject;

import java.util.Collections;

/**
 * Modul pro práci s tásky.
 *
 * @author mimic
 */
@Inject
public final class TaskModule extends Module {

    protected TaskModule(IBotnet botnet) {
        super(botnet);
    }

    /**
     * Získá všechny tásky.
     */
    public synchronized TaskResponse getTasks() {
        var opcode = new TaskOpcode();
        return createTaskResponse(opcode);
    }

    /**
     * Dokončí tásk za netcoins pro dané ID.
     */
    public synchronized TaskResponse finishForNetcoins(int taskId) {
        var opcode = new TaskFinishForNetCoinsOpcode();
        opcode.setTaskId(taskId);

        return createTaskResponse(opcode);
    }

    /**
     * Dokončí všechny tásky za netcoins pro dané ID.
     */
    public synchronized TaskResponse finishAllForNetcoins(int taskId) {
        var opcode = new TasksFinishForNetCoinsOpcode();
        opcode.setTaskId(taskId);

        return createTaskResponse(opcode);
    }

    /**
     * Použije boost na tásk.
     */
    public synchronized TaskResponse boostTask(int taskId) {
        var opcode = new TaskBoostOpcode();
        opcode.setTaskId(taskId);

        return createTaskResponse(opcode);
    }

    /**
     * Použije 5x boost na tásk.
     */
    public synchronized TaskResponse boost5xTask(int taskId) {
        var opcode = new TaskBoost5xOpcode();
        opcode.setTaskId(taskId);

        return createTaskResponse(opcode);
    }

    /**
     * Zruší bruteforce.
     */
    public synchronized TaskResponse abortBruteforce(long bruteId) {
        var opcode = new AbortBruteOpcode();
        opcode.setBruteforceId(bruteId);

        return createTaskResponse(opcode);
    }

    /**
     * Zruší aktualizaci tásku.
     */
    public synchronized TaskResponse abortTask(int taskId) {
        var opcode = new AbortTaskOpcode();
        opcode.setTaskId(taskId);

        return createTaskResponse(opcode);
    }

    /**
     * Dokončí bruteforce za netcoins.
     */
    public synchronized TaskResponse finishBruteforceForNetcoins(long bruteId) {
        var opcode = new FinishBruteForNetcoinsOpcode();
        opcode.setBruteforceId(bruteId);

        return createTaskResponse(opcode);
    }

    /**
     * Odstraní bruteforce z tásků.
     */
    public synchronized TaskResponse removeBruteforce(long bruteId) {
        var opcode = new RemoveBruteOpcode();
        opcode.setBruteforceId(bruteId);

        return createTaskResponse(opcode);
    }

    /**
     * Zkusí neůspěšný bruteforce znovu.
     */
    public synchronized TaskResponse retryBruteforce(String targetIp) {
        var opcode = new RetryBruteOpcode();
        opcode.setTargetIp(targetIp);

        return createTaskResponse(opcode);
    }

    private TaskResponse createTaskResponse(Opcode opcode) {
        var response = sendRequest(opcode);

        var dto = new TaskResponse();
        ModuleHelper.checkResponseIntegrity(response.keySet(), TaskResponse.class);
        ModuleHelper.setField(response, dto, TaskResponse.P_FINISH_ALL);
        ModuleHelper.setField(response, dto, TaskResponse.P_ABORTED);
        ModuleHelper.setField(response, dto, TaskResponse.P_BRUTE_REMOVED);
        ModuleHelper.setField(response, dto, TaskResponse.P_BRUTE_ABORTED);
        ModuleHelper.setField(response, dto, TaskResponse.P_BRUTE_FINISHED);
        ModuleHelper.setField(response, dto, TaskResponse.P_FINISHED);
        ModuleHelper.setField(response, dto, TaskResponse.P_BRUTE_RETRY);
        ModuleHelper.setField(response, dto, TaskResponse.P_BOOSTED);
        ModuleHelper.setField(response, dto, TaskResponse.P_LEVEL_UP);
        ModuleHelper.setField(response, dto, TaskResponse.P_UPDATE_COUNT);
        ModuleHelper.setField(response, dto, TaskResponse.P_BRUTE_COUNT);
        ModuleHelper.setField(response, dto, TaskResponse.P_NEXT_DONE);
        ModuleHelper.setField(response, dto, TaskResponse.P_NEXT_DONE_2);
        ModuleHelper.setField(response, dto, TaskResponse.P_LEVEL);
        ModuleHelper.setField(response, dto, TaskResponse.P_NETCOINS);
        ModuleHelper.setField(response, dto, TaskResponse.P_BOOSTERS);
        ModuleHelper.setField(response, dto, TaskResponse.P_FINISH_ALL_COSTS);

        if (!ModuleHelper.setField(response, dto, TaskResponse.P_BRUTED_IPS, (f, data) -> ModuleHelper
                .convertToIpBruteDetailData(data))) {
            dto.setBrutedIps(Collections.emptyList());
        }
        if (!ModuleHelper.setField(response, dto, TaskResponse.P_UPDATES, (f, data) -> ModuleHelper
                .convertToTaskUpdateData(data))) {
            dto.setUpdates(Collections.emptyList());
        }
        return dto;
    }
}
