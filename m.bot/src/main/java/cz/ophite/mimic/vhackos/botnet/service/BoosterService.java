package cz.ophite.mimic.vhackos.botnet.service;

import cz.ophite.mimic.vhackos.botnet.Botnet;
import cz.ophite.mimic.vhackos.botnet.api.module.TaskModule;
import cz.ophite.mimic.vhackos.botnet.api.net.response.TaskResponse;
import cz.ophite.mimic.vhackos.botnet.api.net.response.data.TaskUpdateData;
import cz.ophite.mimic.vhackos.botnet.service.base.EndpointService;
import cz.ophite.mimic.vhackos.botnet.service.base.IService;
import cz.ophite.mimic.vhackos.botnet.service.base.Service;
import cz.ophite.mimic.vhackos.botnet.shared.dto.AppStoreType;
import cz.ophite.mimic.vhackos.botnet.shared.injection.Autowired;
import cz.ophite.mimic.vhackos.botnet.shared.injection.Inject;
import cz.ophite.mimic.vhackos.botnet.shared.utils.SharedUtils;

/**
 * Aplikuje boosty na tásky.
 *
 * @author mimic
 */
@Inject
@EndpointService(IService.SERVICE_BOOSTER)
public final class BoosterService extends Service {

    @Autowired
    private TaskModule taskModule;

    protected BoosterService(Botnet botnet) {
        super(botnet);
    }

    @Override
    public String getDescription() {
        return "Apply boosters to active tasks";
    }

    @Override
    protected void initialize() {
        setTimeout(getConfig().getBoosterTimeout());
    }

    @Override
    protected void execute() {
        var resp = taskModule.getTasks();
        getShared().setTaskResponse(resp);

        if (resp == null || resp.getBoosters() <= getConfig().getSafeBoosters() || resp.getUpdates().isEmpty()) {
            if (resp != null) {
                resp = finishForNetcoins(resp);
            }
            printEndMessage(resp);
            return;
        }
        var data = getLongestLastingTask(resp);
        var seconds = data.getEnd() - data.getNow();

        while (seconds > getConfig().getBoosterReqTime()) {
            sleep();
            resp = taskModule.boostTask(data.getAppId());
            getShared().setTaskResponse(resp);
            data = getLongestLastingTask(resp);
            seconds = data.getEnd() - data.getNow();
            getLog().info("Boost used. The longest time is now: {}", SharedUtils.toTimeFormat(seconds * 1000));
        }
        resp = finishForNetcoins(resp);
        printEndMessage(resp);
    }

    private TaskUpdateData getLongestLastingTask(TaskResponse resp) {
        var maxEnd = Long.MIN_VALUE;
        TaskUpdateData data = null;

        for (var update : resp.getUpdates()) {
            if (update.getEnd() > maxEnd) {
                maxEnd = update.getEnd();
                data = update;
            }
        }
        return data;
    }

    private TaskResponse finishForNetcoins(TaskResponse resp) {
        if (getConfig().isBoosterUseNetcoins()) {
            if (resp.getUpdateCount() > 0) {
                if (resp.getNetCoins() <= getConfig().getSafeNetcoins()) {
                    return resp;
                }
                var updates = resp.getUpdates();

                for (var update : updates) {
                    var remaining = update.getEnd() - update.getNow();

                    if (remaining <= getConfig().getBoosterMaxTimeForNetcoins()) {
                        sleep();
                        resp = taskModule.finishForNetcoins(update.getTaskId());

                        var type = AppStoreType.getById(update.getAppId());
                        getLog().info("The '{}' update was finished with netcoins. Remaining netcoins: {}", type
                                .getAlias(), resp.getNetCoins());
                    }
                }
            }
        }
        return resp;
    }

    private void printEndMessage(TaskResponse resp) {
        if (isRunningAsync()) {
            getLog().info("Done. Remaining {} boosters. Next check will be in: {}", resp.getBoosters(), SharedUtils
                    .toTimeFormat(getTimeout()));
        }
    }
}
