package cz.ophite.mimic.vhackos.botnet.api.module;

import cz.ophite.mimic.vhackos.botnet.api.IBotnet;
import cz.ophite.mimic.vhackos.botnet.api.module.base.Module;
import cz.ophite.mimic.vhackos.botnet.api.module.base.ModuleHelper;
import cz.ophite.mimic.vhackos.botnet.api.net.response.BuyResponse;
import cz.ophite.mimic.vhackos.botnet.api.opcode.BuyListOpcode;
import cz.ophite.mimic.vhackos.botnet.api.opcode.BuyNewIpOpcode;
import cz.ophite.mimic.vhackos.botnet.api.opcode.base.Opcode;
import cz.ophite.mimic.vhackos.botnet.shared.injection.Inject;

import java.util.Collections;

/**
 * Stará se o nákupy za peníze nebo netcoins.
 *
 * @author mimic
 */
@Inject
public final class BuyModule extends Module {

    protected BuyModule(IBotnet botnet) {
        super(botnet);
    }

    /**
     * Získá seznam položek ke koupi.
     */
    public synchronized BuyResponse getBuyList() {
        var opcode = new BuyListOpcode();
        return createBuyResponse(opcode);
    }

    /**
     * Zakoupí novou IP adresu.
     */
    public synchronized BuyResponse buyNewIpAddress() {
        var opcode = new BuyNewIpOpcode();
        return createBuyResponse(opcode);
    }

    private BuyResponse createBuyResponse(Opcode opcode) {
        var response = sendRequest(opcode);
        var dto = new BuyResponse();
        ModuleHelper.checkResponseIntegrity(response.keySet(), BuyResponse.class);
        ModuleHelper.setField(response, dto, BuyResponse.P_INET_UPGRADED);
        ModuleHelper.setField(response, dto, BuyResponse.P_IP_CHANGE);
        ModuleHelper.setField(response, dto, BuyResponse.P_BOUGHT);
        ModuleHelper.setField(response, dto, BuyResponse.P_PURCHASE_LIMIT);
        ModuleHelper.setField(response, dto, BuyResponse.P_INET);
        ModuleHelper.setField(response, dto, BuyResponse.P_INET_COSTS);
        ModuleHelper.setField(response, dto, BuyResponse.P_REQ_LEVEL_INET);
        ModuleHelper.setField(response, dto, BuyResponse.P_NEXT_INET);
        ModuleHelper.setField(response, dto, BuyResponse.P_CAN);
        ModuleHelper.setField(response, dto, BuyResponse.P_CAN_MAX);
        ModuleHelper.setField(response, dto, BuyResponse.P_SKU_COUNT);

        if (!ModuleHelper
                .setField(response, dto, BuyResponse.P_SKUS, (f, data) -> ModuleHelper.convertToSKUsData(data))) {
            dto.setSKUs(Collections.emptyList());
        }

        if (!ModuleHelper
                .setField(response, dto, BuyResponse.P_ITEMS, (f, data) -> ModuleHelper.convertToBuyItemData(data))) {
            dto.setItems(Collections.emptyList());
        }
        return dto;
    }
}
