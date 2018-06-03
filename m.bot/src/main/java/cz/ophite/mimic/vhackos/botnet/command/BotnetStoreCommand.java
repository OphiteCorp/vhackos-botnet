package cz.ophite.mimic.vhackos.botnet.command;

import cz.ophite.mimic.vhackos.botnet.Botnet;
import cz.ophite.mimic.vhackos.botnet.api.module.StoreModule;
import cz.ophite.mimic.vhackos.botnet.api.net.response.AppStoreResponse;
import cz.ophite.mimic.vhackos.botnet.api.net.response.data.AppStoreData;
import cz.ophite.mimic.vhackos.botnet.command.base.BaseCommand;
import cz.ophite.mimic.vhackos.botnet.shared.command.Command;
import cz.ophite.mimic.vhackos.botnet.shared.command.CommandParam;
import cz.ophite.mimic.vhackos.botnet.shared.dto.AppStoreType;
import cz.ophite.mimic.vhackos.botnet.shared.injection.Autowired;
import cz.ophite.mimic.vhackos.botnet.shared.injection.Inject;
import cz.ophite.mimic.vhackos.botnet.shared.utils.SharedUtils;
import cz.ophite.mimic.vhackos.botnet.shared.utils.ascii.AsciiMaker;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * Příkazy kolem obchodu s aplikacema.
 *
 * @author mimic
 */
@Inject
public final class BotnetStoreCommand extends BaseCommand {

    @Autowired
    private StoreModule storeModule;

    protected BotnetStoreCommand(Botnet botnet) {
        super(botnet);
    }

    /**
     * Získá informace o aplikacích z obchodu.
     */
    @Command(value = "apps", comment = "Gets apps in store")
    private String getApps() {
        return execute("apps", am -> {
            var data = storeModule.getApps();
            var fields = getFields(data, true);
            addAppStoreResponseToAsciiMaker(am, fields);
        });
    }

    /**
     * Zakoupí jednu aplikaci.
     */
    @Command(value = "app buy", comment = "Purchases one app in the store")
    private String buyApplication(@CommandParam("appCode") String appCode) {
        return execute("app buy -> " + appCode, am -> {
            var appType = AppStoreType.getByCode(appCode);

            if (appType == null || (appType != null && !appType.isUpdatable())) {
                var updatableApps = AppStoreType.UPDATABLE_APP_CODES;
                put(am, "Error", "The application code is not valid. Available updatable codes are: " + updatableApps);
            } else {
                var data = storeModule.buyApp(appType);
                var fields = getFields(data, true);
                addAppStoreResponseToAsciiMaker(am, fields);
            }
        });
    }

    /**
     * Zakoupí všechny aplikace (podle počtu volných tásků).
     */
    @Command(value = "app buy all", comment = "Purchases all app in the store")
    private String buyAllApplication(@CommandParam("appCode") String appCode) {
        return execute("app buy all -> " + appCode, am -> {
            var appType = AppStoreType.getByCode(appCode);

            if (appType == null || (appType != null && !appType.isUpdatable())) {
                var updatableApps = AppStoreType.UPDATABLE_APP_CODES;
                put(am, "Error", "The application code is not valid. Available updatable codes are: " + updatableApps);
            } else {
                var data = storeModule.buyAllApp(appType);
                var fields = getFields(data, true);
                addAppStoreResponseToAsciiMaker(am, fields);
            }
        });
    }

    // === Pomocné metody
    // ================================================================================================================

    private void addAppStoreResponseToAsciiMaker(AsciiMaker am, Map<String, FieldData> fields) {
        put(am, fields.remove(AppStoreResponse.P_MONEY));
        put(am, fields.remove(AppStoreResponse.P_LEVEL));

        convertAppStore(am, fields.remove(AppStoreResponse.P_APPS));

        putRemainings(am, fields);
    }

    private void convertAppStore(AsciiMaker am, FieldData data) {
        var apps = (List<AppStoreData>) data.value;
        apps.sort(Comparator.comparing(AppStoreData::getAppId));
        var otherApps = new ArrayList<AppStoreData>();

        for (var it = apps.iterator(); it.hasNext(); ) {
            var app = it.next();

            if (!AppStoreType.getById(app.getAppId()).isUpdatable()) {
                otherApps.add(app);
                it.remove();
            }
        }
        insertAppsToAsciiMaker(am, apps, data.name);
        am.addRule();
        insertAppsToAsciiMaker(am, otherApps, "Other");
    }

    private void insertAppsToAsciiMaker(AsciiMaker am, List<AppStoreData> apps, String name) {
        for (var i = 0; i < apps.size(); i++) {
            var app = apps.get(i);
            var type = AppStoreType.getById(app.getAppId());
            var fRunning = (app.getRunning() != null && app.getRunning() > 0);
            var fValue = type.isUpdatable() ? app.getLevel().toString() : SharedUtils.convertToBoolean(app.getLevel());
            String str;

            if (fRunning) {
                str = String.format("%s %s | %s Running", StringUtils.rightPad(type.getAlias(), 24), StringUtils
                        .leftPad(fValue, 5), StringUtils.leftPad(app.getRunning().toString(), 2));
            } else {
                str = String.format("%s %s", StringUtils.rightPad(type.getAlias(), 24), StringUtils.leftPad(fValue, 5));
            }
            put(am, (i == 0) ? name : "", str);
        }
    }
}
