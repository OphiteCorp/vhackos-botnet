package cz.ophite.mimic.vhackos.botnet.service;

import cz.ophite.mimic.vhackos.botnet.Botnet;
import cz.ophite.mimic.vhackos.botnet.api.module.StoreModule;
import cz.ophite.mimic.vhackos.botnet.api.module.TaskModule;
import cz.ophite.mimic.vhackos.botnet.api.net.response.AppStoreResponse;
import cz.ophite.mimic.vhackos.botnet.api.net.response.data.AppStoreData;
import cz.ophite.mimic.vhackos.botnet.service.base.EndpointService;
import cz.ophite.mimic.vhackos.botnet.service.base.IService;
import cz.ophite.mimic.vhackos.botnet.service.base.Service;
import cz.ophite.mimic.vhackos.botnet.shared.dto.AppStoreType;
import cz.ophite.mimic.vhackos.botnet.shared.injection.Autowired;
import cz.ophite.mimic.vhackos.botnet.shared.injection.Inject;
import cz.ophite.mimic.vhackos.botnet.shared.utils.SharedUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Služba pro nákup aplikací.
 *
 * @author mimic
 */
@Inject
@EndpointService(IService.SERVICE_STORE)
public final class StoreService extends Service {

    @Autowired
    private StoreModule storeModule;

    @Autowired
    private TaskModule taskModule;

    protected StoreService(Botnet botnet) {
        super(botnet);
    }

    @Override
    public String getDescription() {
        return "Automatically buys and updates applications";
    }

    @Override
    protected void initialize() {
        setTimeout(getConfig().getStoreTimeout());
    }

    @Override
    protected void execute() {
        var updatedApps = getConfig().getUpdatedAppsList();
        var freeTasks = getFreeTasks();

        if (updatedApps.isEmpty() || freeTasks == 0) {
            if (isRunningAsync()) {
                getLog().info("Next check will be in: {}", SharedUtils.toTimeFormat(getTimeout()));
            }
            return;
        }
        var resp = storeModule.getApps();
        var resultApps = getAppList(resp);

        try {
            freeTasks = updateProcess(resp, resultApps, freeTasks);

            // všechny aplikace mají stejnou úroveň a stály jsou volné tásky, takže je postupně rozhodíme
            while (freeTasks > 0) {
                for (var app : resultApps) {
                    app.levelLeft++;
                }
                try {
                    freeTasks = updateProcess(resp, resultApps, freeTasks);
                } catch (OutOfMoneyException e) {
                    break;
                }
            }
        } catch (OutOfMoneyException e) {
            // nic
        }
        // nutné pro aktualizaci volných tásků
        sleep();
        var tasks = taskModule.getTasks();
        getShared().setTaskResponse(tasks);

        if (isRunningAsync()) {
            getLog().info("Done. Next check will be in: {}", SharedUtils.toTimeFormat(getTimeout()));
        }
    }

    private int updateProcess(AppStoreResponse resp, List<App> resultApps, int freeTasks) {
        for (var app : resultApps) {
            if (freeTasks == 0) {
                break;
            }
            // aplikaci zbývá vylepšit více úrovní než je počet dostupných volných tásků
            if (app.levelLeft > 0) {
                var totalCost = ((app.data.getBasePrice() * (freeTasks + app.data.getLevel())) / 100) * freeTasks;

                // nejsou peníze pro aktualizaci počtu aplikací roven počtu volných tásků
                if (totalCost > resp.getMoney()) {
                    while (freeTasks > 0 && app.levelLeft > 0) {
                        // banka má peníze na aktualizaci jedné aplikace
                        if (resp.getMoney() >= app.data.getPrice()) {
                            sleep();
                            resp = storeModule.buyApp(app.type);
                            var newApp = createApp(resp, app.type);
                            app.update(newApp);
                            freeTasks--;
                            getLog().info("Purchased '{}' for {} to level {}. Remain money: {}", app.type
                                    .getAlias(), app.data.getPrice(), newApp.data.getLevel(), resp.getMoney());
                        } else {
                            getLog().info("Bank does not have enough money to update app '{}'. Needed: {}. You have: {}", app.type
                                    .getAlias(), app.data.getPrice(), resp.getMoney());
                            throw new OutOfMoneyException();
                        }
                    }
                } else {
                    // jsou peníze pro hromadný nákup jedné aplikace
                    sleep();
                    resp = storeModule.buyAllApp(app.type);
                    var newApp = createApp(resp, app.type);
                    app.update(newApp);
                    freeTasks = 0;
                    getLog().info("Purchased '{}' for {} to level {}. Remain money: {}", app.type.getAlias(), app.data
                            .getPrice(), newApp.data.getLevel(), resp.getMoney());
                    break;
                }
            }
        }
        return freeTasks;
    }

    private int getFreeTasks() {
        if (getShared().getTaskResponse() == null) {
            var resp = taskModule.getTasks();
            getShared().setTaskResponse(resp);
            sleep();
        }
        return getShared().getMaxTaskUpdates() - getShared().getTaskResponse().getUpdateCount();
    }

    private App createApp(AppStoreResponse resp, AppStoreType type) {
        var list = getAppList(resp);
        for (var app : list) {
            if (app.type == type) {
                return app;
            }
        }
        return null;
    }

    private List<App> getAppList(AppStoreResponse resp) {
        var list = new ArrayList<App>();

        for (var app : getConfig().getUpdatedAppsList()) {
            var result = resp.getApps().stream().filter(p -> AppStoreType.getById(p.getAppId()) == app)
                    .collect(Collectors.toList());

            if (!result.isEmpty()) {
                var a = new App();
                a.type = app;
                a.data = result.get(0);
                list.add(a);
            }
        }
        list.sort(Comparator.comparing(o -> o.data.getLevel()));

        var last = list.get(list.size() - 1);
        for (var app : list) {
            app.levelLeft = last.data.getLevel() - app.data.getLevel();
        }
        return list;
    }

    private static final class App {

        private AppStoreData data;
        private AppStoreType type;
        private int levelLeft;

        private void update(App app) {
            data = app.data;
            type = app.type;
            levelLeft = app.levelLeft;
        }
    }

    private static final class OutOfMoneyException extends RuntimeException {
        // nic
    }
}
