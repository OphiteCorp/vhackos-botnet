package cz.ophite.mimic.vhackos.botnet.api.module;

import cz.ophite.mimic.vhackos.botnet.api.IBotnet;
import cz.ophite.mimic.vhackos.botnet.api.module.base.Module;
import cz.ophite.mimic.vhackos.botnet.api.module.base.ModuleHelper;
import cz.ophite.mimic.vhackos.botnet.api.net.response.AppStoreResponse;
import cz.ophite.mimic.vhackos.botnet.api.opcode.AppStoreOpcode;
import cz.ophite.mimic.vhackos.botnet.api.opcode.BuyAllAppOpcode;
import cz.ophite.mimic.vhackos.botnet.api.opcode.BuyAppOpcode;
import cz.ophite.mimic.vhackos.botnet.shared.dto.AppStoreType;
import cz.ophite.mimic.vhackos.botnet.shared.injection.Inject;

import java.util.Collections;
import java.util.Map;

/**
 * Modul pro obchod aplikací.
 *
 * @author mimic
 */
@Inject
public final class StoreModule extends Module {

    protected StoreModule(IBotnet botnet) {
        super(botnet);
    }

    /**
     * Získá aplikace v obchodu.
     */
    public synchronized AppStoreResponse getApps() {
        var opcode = new AppStoreOpcode();
        var response = sendRequest(opcode);
        return createAppStoreResponse(response);
    }

    /**
     * Zakoupí jednu aplikaci v obchodě.
     */
    public synchronized AppStoreResponse buyApp(AppStoreType app) {
        var opcode = new BuyAppOpcode();
        opcode.setApp(app);

        var response = sendRequest(opcode);
        return createAppStoreResponse(response);
    }

    /**
     * Zakoupí všechny aplikace (dle volných tásků).
     */
    public synchronized AppStoreResponse buyAllApp(AppStoreType app) {
        var opcode = new BuyAllAppOpcode();
        opcode.setApp(app);

        var response = sendRequest(opcode);
        return createAppStoreResponse(response);
    }

    private AppStoreResponse createAppStoreResponse(Map<String, Object> response) {
        var dto = new AppStoreResponse();
        ModuleHelper.checkResponseIntegrity(response.keySet(), AppStoreResponse.class);
        ModuleHelper.setField(response, dto, AppStoreResponse.P_LEVEL_UP);
        ModuleHelper.setField(response, dto, AppStoreResponse.P_UPDATED);
        ModuleHelper.setField(response, dto, AppStoreResponse.P_INSTALLED);
        ModuleHelper.setField(response, dto, AppStoreResponse.P_FILLED);
        ModuleHelper.setField(response, dto, AppStoreResponse.P_LEVEL);
        ModuleHelper.setField(response, dto, AppStoreResponse.P_MONEY);
        ModuleHelper.setField(response, dto, AppStoreResponse.P_SPAM);
        ModuleHelper.setField(response, dto, AppStoreResponse.P_APP_COUNT);

        if (!ModuleHelper.setField(response, dto, AppStoreResponse.P_APPS, (f, data) -> ModuleHelper
                .convertToAppStoreData(data))) {
            dto.setApps(Collections.emptyList());
        }
        return dto;
    }
}
