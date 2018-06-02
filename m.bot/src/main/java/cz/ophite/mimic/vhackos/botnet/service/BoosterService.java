package cz.ophite.mimic.vhackos.botnet.service;

import cz.ophite.mimic.vhackos.botnet.Botnet;
import cz.ophite.mimic.vhackos.botnet.api.module.TaskModule;
import cz.ophite.mimic.vhackos.botnet.api.net.response.TaskResponse;
import cz.ophite.mimic.vhackos.botnet.api.net.response.data.TaskUpdateData;
import cz.ophite.mimic.vhackos.botnet.service.base.EndpointService;
import cz.ophite.mimic.vhackos.botnet.service.base.IService;
import cz.ophite.mimic.vhackos.botnet.service.base.Service;
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

        if (resp.getBoosters() <= getConfig().getSafeBoosters() || resp.getUpdates().isEmpty()) {
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

    private void printEndMessage(TaskResponse resp) {
        if (isRunningAsync()) {
            getLog().info("Done. Remaining {} boosters. Next check will be in: {}", resp.getBoosters(), SharedUtils
                    .toTimeFormat(getTimeout()));
        }
    }
}
