package cz.ophite.mimic.vhackos.botnet.service;

import cz.ophite.mimic.vhackos.botnet.Botnet;
import cz.ophite.mimic.vhackos.botnet.api.module.TaskModule;
import cz.ophite.mimic.vhackos.botnet.service.base.EndpointService;
import cz.ophite.mimic.vhackos.botnet.service.base.IService;
import cz.ophite.mimic.vhackos.botnet.service.base.Service;
import cz.ophite.mimic.vhackos.botnet.shared.injection.Autowired;
import cz.ophite.mimic.vhackos.botnet.shared.injection.Inject;
import cz.ophite.mimic.vhackos.botnet.shared.utils.SharedUtils;

/**
 * Služba, která vykonává aktualizace účtu pro udržení aktuálních dat.
 *
 * @author mimic
 */
@Inject
@EndpointService(IService.SERVICE_UPDATE)
public final class UpdateService extends Service {

    @Autowired
    private TaskModule taskModule;

    protected UpdateService(Botnet botnet) {
        super(botnet);
    }

    @Override
    public String getDescription() {
        return "Keeps user information up to date";
    }

    @Override
    protected void initialize() {
        setTimeout(getConfig().getUpdateTimeout());
    }

    @Override
    protected void execute() {
        var updateResp = getCommonModule().update();
        getShared().setUpdateResponse(updateResp);
        sleep();

        var tasksResp = taskModule.getTasks();
        getShared().setTaskResponse(tasksResp);

        if (isRunningAsync()) {
            getLog().info("Profile updated. Next update will be in: {}", SharedUtils.toTimeFormat(getTimeout()));
        }
    }
}
