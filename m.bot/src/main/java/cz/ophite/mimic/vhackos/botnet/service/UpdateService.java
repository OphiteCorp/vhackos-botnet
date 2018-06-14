package cz.ophite.mimic.vhackos.botnet.service;

import cz.ophite.mimic.vhackos.botnet.Botnet;
import cz.ophite.mimic.vhackos.botnet.api.module.StoreModule;
import cz.ophite.mimic.vhackos.botnet.api.module.TaskModule;
import cz.ophite.mimic.vhackos.botnet.service.base.EndpointService;
import cz.ophite.mimic.vhackos.botnet.service.base.IService;
import cz.ophite.mimic.vhackos.botnet.service.base.Service;
import cz.ophite.mimic.vhackos.botnet.shared.dto.AppStoreType;
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

    @Autowired
    private StoreModule storeModule;

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

        // zakoupí aplikace, které se zobrazují v navigaci (v menu) a které nelze dále vylepšovat
        if (updateResp != null) {
            buyMenuApplications(updateResp.getLevel(), updateResp.getMoney());
        }
        if (isRunningAsync()) {
            getLog().info("Profile updated. Next update will be in: {}", SharedUtils.toTimeFormat(getTimeout()));
        }
    }

    private void buyMenuApplications(int playerLevel, long money) {
        sleep();
        var appsResp = storeModule.getApps();

        for (var app : appsResp.getApps()) {
            var type = AppStoreType.getById(app.getAppId());

            // je možné zakoupit aplikaci?
            if (playerLevel >= app.getRequireLevel() && app.getLevel() == 0) {
                if (money >= app.getPrice()) {
                    sleep();
                    var resp = storeModule.buyMenuApp(type);

                    if (SharedUtils.toBoolean(resp.getInstalled())) {
                        getLog().info("A new application was purchased: {}", type.getAlias());
                    }
                } else {
                    getLog().warn("A new '{}' app is available, but you do not have the money to buy", type.getAlias());
                }
            }
        }
    }
}
