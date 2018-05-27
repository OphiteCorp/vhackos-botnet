package cz.ophite.mimic.vhackos.botnet.command;

import cz.ophite.mimic.vhackos.botnet.api.module.TaskModule;
import cz.ophite.mimic.vhackos.botnet.api.net.response.TaskResponse;
import cz.ophite.mimic.vhackos.botnet.api.net.response.data.IpBruteDetailData;
import cz.ophite.mimic.vhackos.botnet.api.net.response.data.TaskUpdateData;
import cz.ophite.mimic.vhackos.botnet.command.base.BaseCommand;
import cz.ophite.mimic.vhackos.botnet.db.service.DatabaseService;
import cz.ophite.mimic.vhackos.botnet.shared.command.Command;
import cz.ophite.mimic.vhackos.botnet.shared.command.CommandParam;
import cz.ophite.mimic.vhackos.botnet.shared.dto.AppStoreType;
import cz.ophite.mimic.vhackos.botnet.shared.injection.Autowired;
import cz.ophite.mimic.vhackos.botnet.shared.injection.Inject;
import cz.ophite.mimic.vhackos.botnet.shared.utils.SharedUtils;
import cz.ophite.mimic.vhackos.botnet.shared.utils.ascii.AsciiMaker;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * Příkazy pro tásky.
 *
 * @author mimic
 */
@Inject
public final class BotnetTaskCommand extends BaseCommand {

    @Autowired
    private DatabaseService databaseService;

    @Autowired
    private TaskModule taskModule;

    /**
     * Získá všechny tásky.
     */
    @Command(value = "tasks", comment = "Gets current user tasks")
    private String getTasks() {
        return execute("tasks", am -> {
            var data = taskModule.getTasks();
            var fields = getFields(data, true);
            addTasksResponseToAsciiMaker(am, fields);
        });
    }

    /**
     * Dokončí tásk podle ID za netcoins.
     */
    @Command(value = "task finish", comment = "Complete task with NetCoins")
    private String finishTask(@CommandParam("taskId") int taskId) {
        return execute("task finish -> " + taskId, am -> {
            var data = taskModule.finishForNetcoins(taskId);
            var fields = getFields(data, true);
            addTasksResponseToAsciiMaker(am, fields);
        });
    }

    /**
     * Dokončí všechny tásky podle ID za netcoins.
     */
    @Command(value = "tasks finish all", comment = "Complete all tasks with NetCoins")
    private String finishAllTask(@CommandParam("taskId") int taskId) {
        return execute("task finish all -> " + taskId, am -> {
            var data = taskModule.finishAllForNetcoins(taskId);
            var fields = getFields(data, true);
            addTasksResponseToAsciiMaker(am, fields);
        });
    }

    /**
     * Použije boost na tásk.
     */
    @Command(value = "task boost", comment = "Uses boost on task")
    private String boostTask(@CommandParam("taskId") int taskId) {
        return execute("task boost -> " + taskId, am -> {
            var data = taskModule.boostTask(taskId);
            var fields = getFields(data, true);
            addTasksResponseToAsciiMaker(am, fields);
        });
    }

    /**
     * Použije boost na tásk.
     */
    @Command(value = "task boost 5x", comment = "Uses 5x boost on task")
    private String boost5xTask(@CommandParam("taskId") int taskId) {
        return execute("task boost 5x -> " + taskId, am -> {
            var data = taskModule.boost5xTask(taskId);
            var fields = getFields(data, true);
            addTasksResponseToAsciiMaker(am, fields);
        });
    }

    /**
     * Zruší bruteforce.
     */
    @Command(value = "task abort brute", comment = "Abort bruteforce")
    private String abortBruteforce(@CommandParam("bruteId") int bruteId) {
        return execute("task abort bruteforce -> " + bruteId, am -> {
            var data = taskModule.abortBruteforce(bruteId);
            var fields = getFields(data, true);
            addTasksResponseToAsciiMaker(am, fields);
        });
    }

    /**
     * Zruší tásk.
     */
    @Command(value = "task abort", comment = "Abort task")
    private String abortTask(@CommandParam("taskId") int taskId) {
        return execute("task abort -> " + taskId, am -> {
            var data = taskModule.abortTask(taskId);
            var fields = getFields(data, true);
            addTasksResponseToAsciiMaker(am, fields);
        });
    }

    /**
     * Dokončí bruteforce za netcoins.
     */
    @Command(value = "task finish brute", comment = "Completes bruteforce for netcoins")
    private String finishBruteforceForNetcoins(@CommandParam("bruteId") int bruteId) {
        return execute("task finish bruteforce -> " + bruteId, am -> {
            var data = taskModule.finishBruteforceForNetcoins(bruteId);
            var fields = getFields(data, true);
            addTasksResponseToAsciiMaker(am, fields);
        });
    }

    /**
     * Odstraní bruteforce z tásků.
     */
    @Command(value = "task remove brute", comment = "Remove bruteforce from tasks")
    private String removeBruteforce(@CommandParam("bruteId") int bruteId) {
        return execute("task remove bruteforce -> " + bruteId, am -> {
            var data = taskModule.removeBruteforce(bruteId);
            var fields = getFields(data, true);
            addTasksResponseToAsciiMaker(am, fields);
        });
    }

    /**
     * Znovu zkusí bruteforce na IP.
     */
    @Command(value = "task retry brute", comment = "Tries bruteforce again")
    private String retryBruteforce(@CommandParam("ip") String ip) {
        return execute("task retry bruteforce -> " + ip, am -> {
            var data = taskModule.retryBruteforce(ip);
            var fields = getFields(data, true);
            addTasksResponseToAsciiMaker(am, fields);
        });
    }

    // === Pomocné metody
    // ================================================================================================================

    private void addTasksResponseToAsciiMaker(AsciiMaker am, Map<String, FieldData> fields) {
        put(am, fields.remove(TaskResponse.P_LEVEL));
        put(am, fields.remove(TaskResponse.P_BOOSTERS));
        put(am, fields.remove(TaskResponse.P_NETCOINS));

        put(am, fields.remove(TaskResponse.P_UPDATE_COUNT));
        convertTaskUpdates(am, fields.remove(TaskResponse.P_UPDATES));

        put(am, fields.remove(TaskResponse.P_BRUTE_COUNT));
        convertBrutedDetailIps(am, fields.remove(TaskResponse.P_BRUTED_IPS));

        putRemainings(am, fields);
    }

    private void convertBrutedDetailIps(AsciiMaker am, FieldData data) {
        var ips = (List<IpBruteDetailData>) data.value;
        var name = data.name;

        for (var i = 0; i < ips.size(); i++) {
            var ip = ips.get(i);
            var fields = getFields(ip, true);
            var fState = fields.get(IpBruteDetailData.P_RESULT).value;
            var left = Math.max(0, ip.getEndTime() - ip.getCurrentTime());
            var leftStr = SharedUtils.toTimeFormat(left * 1000);

            var p1 = ip.getCurrentTime() - ip.getStartTime();
            var p2 = ip.getEndTime() - ip.getStartTime();
            var percent = Math.min(Math.round((float) ((100 * p1) / p2)), 100);

            var str = String.format("%s | %s [ %s ] %s | %s%% | %s", StringUtils
                    .rightPad(ip.getBruteId().toString(), 5), StringUtils.leftPad(ip.getIp(), 15), StringUtils
                    .leftPad(ip.getUserName(), 20), StringUtils.leftPad(leftStr, 15), StringUtils
                    .leftPad(String.valueOf(percent), 4), StringUtils.leftPad(fState.toString(), 7));

            put(am, (i == 0) ? name : "", str);
            databaseService.updateScanIp(ip.getIp(), ip.getUserName(), null);
        }
        if (ips.isEmpty()) {
            put(am, name, "<none>");
        }
    }

    private void convertTaskUpdates(AsciiMaker am, FieldData data) {
        var tasks = (List<TaskUpdateData>) data.value;
        var name = data.name;

        for (var i = 0; i < tasks.size(); i++) {
            var task = tasks.get(i);
            var appType = AppStoreType.getById(task.getAppId());
            var left = Math.max(0, task.getEnd() - task.getNow());
            var leftStr = SharedUtils.toTimeFormat(left * 1000);

            var p1 = task.getNow() - task.getStart();
            var p2 = task.getEnd() - task.getStart();
            var percent = Math.min(Math.round((float) ((100 * p1) / p2)), 100);

            var str = String.format("%s | %s | %s Level | %s | %s%%", StringUtils
                    .rightPad(task.getTaskId().toString(), 5), StringUtils.rightPad(appType.getAlias(), 25), StringUtils
                    .leftPad(task.getLevel().toString(), 4), StringUtils.leftPad(leftStr, 15), StringUtils
                    .leftPad(String.valueOf(percent), 4));

            put(am, (i == 0) ? name : "", str);
        }
        if (tasks.isEmpty()) {
            put(am, name, "<none>");
        }
    }
}
