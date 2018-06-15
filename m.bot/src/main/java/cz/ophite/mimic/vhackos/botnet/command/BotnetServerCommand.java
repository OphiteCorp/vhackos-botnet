package cz.ophite.mimic.vhackos.botnet.command;

import cz.ophite.mimic.vhackos.botnet.Botnet;
import cz.ophite.mimic.vhackos.botnet.api.module.ServerModule;
import cz.ophite.mimic.vhackos.botnet.api.net.response.ServerResponse;
import cz.ophite.mimic.vhackos.botnet.command.base.BaseCommand;
import cz.ophite.mimic.vhackos.botnet.shared.ascii.AsciiMaker;
import cz.ophite.mimic.vhackos.botnet.shared.command.Command;
import cz.ophite.mimic.vhackos.botnet.shared.command.CommandParam;
import cz.ophite.mimic.vhackos.botnet.shared.dto.ServerNodeType;
import cz.ophite.mimic.vhackos.botnet.shared.injection.Autowired;
import cz.ophite.mimic.vhackos.botnet.shared.injection.Inject;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

/**
 * Příkazy pro práci se serverem.
 *
 * @author mimic
 */
@Inject
public final class BotnetServerCommand extends BaseCommand {

    @Autowired
    private ServerModule serverModule;

    protected BotnetServerCommand(Botnet botnet) {
        super(botnet);
    }

    /**
     * Získá informace o serveru.
     */
    @Command(value = "server", comment = "Gets information about the server")
    private String getServer() {
        return execute("server", am -> {
            var data = serverModule.getServer();
            var fields = getFields(data, true, false);
            addServerResponseToAsciiMaker(am, fields);
        });
    }

    /**
     * Přidá nový antivirus node.
     */
    @Command(value = "server add av node", comment = "Adds a new antivirus node")
    private String addAntivirusNode() {
        return execute("server add antivirus node", am -> {
            var data = serverModule.addAntivirusNode();
            var fields = getFields(data, true, false);
            addServerResponseToAsciiMaker(am, fields);
        });
    }

    /**
     * Přidá nový firewall node.
     */
    @Command(value = "server add fw node", comment = "Adds a new firewall node")
    private String addFirewallNode() {
        return execute("server add firewall node", am -> {
            var data = serverModule.addFirewallNode();
            var fields = getFields(data, true, false);
            addServerResponseToAsciiMaker(am, fields);
        });
    }

    /**
     * Aktualizuje 1x node na serveru. Number označuje pořadí nodu (1-3).
     */
    @Command(value = "server update node 1x", comment = "Updates the node on the server 1x")
    private String updateNode1x(@CommandParam("node") String nodeType, @CommandParam("number") int number) {
        var node = ServerNodeType.getByCommand(nodeType);

        return execute("server update node 1x", am -> {
            if (node == null) {
                var commands = ServerNodeType.getCommands();
                put(am, "Error", "The server node code is not valid. Available codes are: " + commands);
            } else {
                var num = (number < 1) ? 1 : (number > 3 ? 3 : number);
                var data = serverModule.updateNode1x(node, num);
                var fields = getFields(data, true, false);
                addServerResponseToAsciiMaker(am, fields);
            }
        });
    }

    /**
     * Aktualizuje 5x node na serveru. Number označuje pořadí nodu (1-3).
     */
    @Command(value = "server update node 5x", comment = "Updates the node on the server 5x")
    private String updateNode5x(@CommandParam("node") String nodeType, @CommandParam("number") int number) {
        var node = ServerNodeType.getByCommand(nodeType);

        return execute("server update node 5x", am -> {
            if (node == null) {
                var commands = ServerNodeType.getCommands();
                put(am, "Error", "The server node code is not valid. Available codes are: " + commands);
            } else {
                var num = (number < 1) ? 1 : (number > 3 ? 3 : number);
                var data = serverModule.updateNode5x(node, num);
                var fields = getFields(data, true, false);
                addServerResponseToAsciiMaker(am, fields);
            }
        });
    }

    /**
     * Otevře jeden balíček.
     */
    @Command(value = "server open package", comment = "Opens one package")
    private String openPackage() {
        return execute("server open package", am -> {
            var data = serverModule.openPackage();
            var fields = getFields(data, true, false);
            addServerResponseToAsciiMaker(am, fields);
        });
    }

    /**
     * Otevře všechny balíčeky.
     */
    @Command(value = "server open all packages", comment = "Opens all packages")
    private String openAllPackages() {
        return execute("server open all packages", am -> {
            var data = serverModule.openAllPackages();
            var fields = getFields(data, true, false);
            addServerResponseToAsciiMaker(am, fields);
        });
    }

    /**
     * Koupí za netcoins N balíčků (max 10).
     */
    @Command(value = "server buy package", comment = "Buys a new server package for netcoins")
    private String buyPackage(@CommandParam("count") int count) {
        return execute("server buy package", am -> {
            var data = serverModule.buyPackages(count);
            var fields = getFields(data, true, false);
            addServerResponseToAsciiMaker(am, fields);
        });
    }

    // === Pomocné metody
    // ================================================================================================================

    private void addServerResponseToAsciiMaker(AsciiMaker am, Map<String, FieldData> fields) {
        put(am, fields.remove(ServerResponse.P_SERVER_FREE_PIECES));
        put(am, fields.remove(ServerResponse.P_AV_FREE_PIECES));
        put(am, fields.remove(ServerResponse.P_FW_FREE_PIECES));

        var server = String.format("%s / %s | %s*", StringUtils
                .leftPad(fields.remove(ServerResponse.P_SERVER_PIECES).value.toString(), 5), StringUtils
                .rightPad(fields.remove(ServerResponse.P_SERVER_MAX_PIECES).value.toString(), 5), fields
                .remove(ServerResponse.P_SERVER_STARS).value);
        put(am, "Server", server);

        var fAv1 = fields.remove(ServerResponse.P_AV1_PIECES).value;
        var fAv1Max = fields.remove(ServerResponse.P_AV1_MAX_PIECES).value;
        if (fAv1Max != null) {
            var av1 = String.format("%s / %s | %s*", StringUtils.leftPad(fAv1.toString(), 5), StringUtils
                    .rightPad(fAv1Max.toString(), 5), fields.remove(ServerResponse.P_AV1_STARS).value);
            put(am, "Antivirus 1", av1);
        }
        var fAv2 = fields.remove(ServerResponse.P_AV2_PIECES).value;
        var fAv2Max = fields.remove(ServerResponse.P_AV2_MAX_PIECES).value;
        if (fAv2Max != null) {
            var av2 = String.format("%s / %s | %s*", StringUtils.leftPad(fAv2.toString(), 5), StringUtils
                    .rightPad(fAv2Max.toString(), 5), fields.remove(ServerResponse.P_AV2_STARS).value);
            put(am, "Antivirus 2", av2);
        }
        var fAv3 = fields.remove(ServerResponse.P_AV3_PIECES).value;
        var fAv3Max = fields.remove(ServerResponse.P_AV3_MAX_PIECES).value;
        if (fAv3Max != null) {
            var av3 = String.format("%s / %s | %s*", StringUtils.leftPad(fAv3.toString(), 5), StringUtils
                    .rightPad(fAv3Max.toString(), 5), fields.remove(ServerResponse.P_AV3_STARS).value);
            put(am, "Antivirus 3", av3);
        }
        var fFw1 = fields.remove(ServerResponse.P_FW1_PIECES).value;
        var fFw1Max = fields.remove(ServerResponse.P_FW1_MAX_PIECES).value;
        if (fFw1Max != null) {
            var fw1 = String.format("%s / %s | %s*", StringUtils.leftPad(fFw1.toString(), 5), StringUtils
                    .rightPad(fFw1Max.toString(), 5), fields.remove(ServerResponse.P_FW1_STARS).value);
            put(am, "Firewall 1", fw1);
        }
        var fFw2 = fields.remove(ServerResponse.P_FW2_PIECES).value;
        var fFw2Max = fields.remove(ServerResponse.P_FW2_MAX_PIECES).value;
        if (fFw2Max != null) {
            var fw2 = String.format("%s / %s | %s*", StringUtils.leftPad(fFw2.toString(), 5), StringUtils
                    .rightPad(fFw2Max.toString(), 5), fields.remove(ServerResponse.P_FW2_STARS).value);
            put(am, "Firewall 2", fw2);
        }
        var fFw3 = fields.remove(ServerResponse.P_FW3_PIECES).value;
        var fFw3Max = fields.remove(ServerResponse.P_FW3_MAX_PIECES).value;
        if (fFw3Max != null) {
            var fw3 = String.format("%s / %s | %s*", StringUtils.leftPad(fFw3.toString(), 5), StringUtils
                    .rightPad(fFw3Max.toString(), 5), fields.remove(ServerResponse.P_FW3_STARS).value);
            put(am, "Firewall 3", fw3);
        }
        var boughtPacks = String.format("%s / %s", StringUtils
                .leftPad(fields.remove(ServerResponse.P_PACKS_BOUGHT).value.toString(), 2), StringUtils
                .rightPad(String.valueOf(ServerResponse.MAX_BOUGHT_PACKS), 2));
        var packs = String.format("%s / %s", StringUtils
                .leftPad(fields.remove(ServerResponse.P_PACKS).value.toString(), 2), StringUtils
                .rightPad(String.valueOf(ServerResponse.MAX_PACKS), 2));
        put(am, "Packs Bought", boughtPacks);
        put(am, "Packs", packs);
        put(am, fields.remove(ServerResponse.P_NEXT_PACK_IN));

        putRemainings(am, fields);
    }
}
