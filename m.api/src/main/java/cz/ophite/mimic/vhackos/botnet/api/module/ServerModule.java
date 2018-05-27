package cz.ophite.mimic.vhackos.botnet.api.module;

import cz.ophite.mimic.vhackos.botnet.api.IBotnet;
import cz.ophite.mimic.vhackos.botnet.api.module.base.Module;
import cz.ophite.mimic.vhackos.botnet.api.module.base.ModuleHelper;
import cz.ophite.mimic.vhackos.botnet.api.net.response.ServerResponse;
import cz.ophite.mimic.vhackos.botnet.api.opcode.*;
import cz.ophite.mimic.vhackos.botnet.api.opcode.base.Opcode;
import cz.ophite.mimic.vhackos.botnet.shared.dto.ServerNodeType;
import cz.ophite.mimic.vhackos.botnet.shared.injection.Inject;

/**
 * Správa serveru.
 *
 * @author mimic
 */
@Inject
public final class ServerModule extends Module {

    protected ServerModule(IBotnet botnet) {
        super(botnet);
    }

    /**
     * Získá informace o serveru.
     */
    public synchronized ServerResponse getServer() {
        var opcode = new ServerOpcode();
        return createServerResponse(opcode);
    }

    /**
     * Přidá node pro antivirus.
     */
    public synchronized ServerResponse addAntivirusNode() {
        var opcode = new ServerAddAvNodeOpcode();
        return createServerResponse(opcode);
    }

    /**
     * Přidá node pro firewall.
     */
    public synchronized ServerResponse addFirewallNode() {
        var opcode = new ServerAddFwNodeOpcode();
        return createServerResponse(opcode);
    }

    /**
     * Aktualizuje node 1x.
     */
    public synchronized ServerResponse updateNode1x(ServerNodeType nodeType, int number) {
        var opcode = new ServerUpdateNode1xOpcode();
        opcode.setNodeType(nodeType);
        opcode.setNodeNumber(number);

        return createServerResponse(opcode);
    }

    /**
     * Aktualizuje node 5x.
     */
    public synchronized ServerResponse updateNode5x(ServerNodeType nodeType, int number) {
        var opcode = new ServerUpdateNode5xOpcode();
        opcode.setNodeType(nodeType);
        opcode.setNodeNumber(number);

        return createServerResponse(opcode);
    }

    /**
     * Otevře 1 balíček.
     */
    public synchronized ServerResponse openPackage() {
        var opcode = new ServerOpenPackageOpcode();
        return createServerResponse(opcode);
    }

    /**
     * Otevře všechny balíčky.
     */
    public synchronized ServerResponse openAllPackages() {
        var opcode = new ServerOpenAllPackagesOpcode();
        return createServerResponse(opcode);
    }

    /**
     * Koupí N(1-10) balíčků.
     */
    public synchronized ServerResponse buyPackages(int count) {
        var opcode = new ServerBuyPackageOpcode();
        opcode.setCount(count);

        return createServerResponse(opcode);
    }

    private ServerResponse createServerResponse(Opcode opcode) {
        var response = sendRequest(opcode);
        var dto = new ServerResponse();

        ModuleHelper.checkResponseIntegrity(response.keySet(), ServerResponse.class);
        ModuleHelper.setField(response, dto, ServerResponse.P_AV_ADDED);
        ModuleHelper.setField(response, dto, ServerResponse.P_FW_ADDED);
        ModuleHelper.setField(response, dto, ServerResponse.P_NODE_UPDATED);
        ModuleHelper.setField(response, dto, ServerResponse.P_SPACK_OPEN);
        ModuleHelper.setField(response, dto, ServerResponse.P_SPACK_OPEN_ALL);
        ModuleHelper.setField(response, dto, ServerResponse.P_BOUGHT_ONE);
        ModuleHelper.setField(response, dto, ServerResponse.P_BOUGHT_TEN);
        ModuleHelper.setField(response, dto, ServerResponse.P_PACKS_BOUGHT);
        ModuleHelper.setField(response, dto, ServerResponse.P_BOUGHT_LIMIT);
        ModuleHelper.setField(response, dto, ServerResponse.P_SERVER_PIECES);
        ModuleHelper.setField(response, dto, ServerResponse.P_AV1_PIECES);
        ModuleHelper.setField(response, dto, ServerResponse.P_AV2_PIECES);
        ModuleHelper.setField(response, dto, ServerResponse.P_AV3_PIECES);
        ModuleHelper.setField(response, dto, ServerResponse.P_FW1_PIECES);
        ModuleHelper.setField(response, dto, ServerResponse.P_FW2_PIECES);
        ModuleHelper.setField(response, dto, ServerResponse.P_FW3_PIECES);
        ModuleHelper.setField(response, dto, ServerResponse.P_SERVER_FREE_PIECES);
        ModuleHelper.setField(response, dto, ServerResponse.P_AV_FREE_PIECES);
        ModuleHelper.setField(response, dto, ServerResponse.P_FW_FREE_PIECES);
        ModuleHelper.setField(response, dto, ServerResponse.P_PACKS);
        ModuleHelper.setField(response, dto, ServerResponse.P_SERVER_MAX_PIECES);
        ModuleHelper.setField(response, dto, ServerResponse.P_SERVER_STARS);
        ModuleHelper.setField(response, dto, ServerResponse.P_AV1_MAX_PIECES);
        ModuleHelper.setField(response, dto, ServerResponse.P_AV1_STARS);
        ModuleHelper.setField(response, dto, ServerResponse.P_AV2_MAX_PIECES);
        ModuleHelper.setField(response, dto, ServerResponse.P_AV2_STARS);
        ModuleHelper.setField(response, dto, ServerResponse.P_AV3_MAX_PIECES);
        ModuleHelper.setField(response, dto, ServerResponse.P_AV3_STARS);
        ModuleHelper.setField(response, dto, ServerResponse.P_FW1_MAX_PIECES);
        ModuleHelper.setField(response, dto, ServerResponse.P_FW1_STARS);
        ModuleHelper.setField(response, dto, ServerResponse.P_FW2_MAX_PIECES);
        ModuleHelper.setField(response, dto, ServerResponse.P_FW2_STARS);
        ModuleHelper.setField(response, dto, ServerResponse.P_FW3_MAX_PIECES);
        ModuleHelper.setField(response, dto, ServerResponse.P_FW3_STARS);
        ModuleHelper.setField(response, dto, ServerResponse.P_AV_NODES);
        ModuleHelper.setField(response, dto, ServerResponse.P_FW_NODES);
        ModuleHelper.setField(response, dto, ServerResponse.P_NEXT_PACK_IN);
        ModuleHelper.setField(response, dto, ServerResponse.P_NODE_TYPE);
        ModuleHelper.setField(response, dto, ServerResponse.P_NODE_NUMBER);
        ModuleHelper.setField(response, dto, ServerResponse.P_SPACK_TYPE);
        ModuleHelper.setField(response, dto, ServerResponse.P_SHOW_MUCH);
        return dto;
    }
}
