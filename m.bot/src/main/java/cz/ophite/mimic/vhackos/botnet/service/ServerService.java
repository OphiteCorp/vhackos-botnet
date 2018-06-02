package cz.ophite.mimic.vhackos.botnet.service;

import cz.ophite.mimic.vhackos.botnet.Botnet;
import cz.ophite.mimic.vhackos.botnet.api.module.ServerModule;
import cz.ophite.mimic.vhackos.botnet.api.net.response.ServerResponse;
import cz.ophite.mimic.vhackos.botnet.service.base.EndpointService;
import cz.ophite.mimic.vhackos.botnet.service.base.IService;
import cz.ophite.mimic.vhackos.botnet.service.base.Service;
import cz.ophite.mimic.vhackos.botnet.shared.dto.ServerNodeType;
import cz.ophite.mimic.vhackos.botnet.shared.injection.Autowired;
import cz.ophite.mimic.vhackos.botnet.shared.injection.Inject;
import cz.ophite.mimic.vhackos.botnet.shared.utils.SharedUtils;

/**
 * Služba pro kontrolu serveru a jeho aktualizaci.
 *
 * @author mimic
 */
@Inject
@EndpointService(IService.SERVICE_SERVER)
public final class ServerService extends Service {

    private static final int NEW_NODE_COST = 100;
    private static final int MAX_NODES = 3;
    private static final int MAX_BOUGHT_PACKAGES = 50;
    private static final int PACKAGE_COST = 40;

    @Autowired
    private ServerModule serverModule;

    private int updateLimit;
    private int coreUpdateLimit;

    protected ServerService(Botnet botnet) {
        super(botnet);
    }

    @Override
    public String getDescription() {
        return "Manages the server";
    }

    @Override
    protected void initialize() {
        setTimeout(getConfig().getServerTimeout());
        updateLimit = getConfig().getServerUpdateLimit();
        coreUpdateLimit = getConfig().getServerCoreUpdateLimit();
    }

    @Override
    protected void execute() {
        if (!SharedUtils.toBoolean(getShared().getUpdateResponse().getServer())) {
            return;
        }
        var resp = serverModule.getServer();
        getLog().info("Available parts -> Server: {}, Antivirus: {}, Firewall: {}", resp.getServerFreePieces(), resp
                .getAvFreePieces(), resp.getFwFreePieces());

        resp = processAntivirus(resp);
        resp = processFirewall(resp);
        resp = processServer(resp);
        resp = processCollectPackages(resp);
        processBuyAndCollectPackages(resp);

        if (isRunningAsync()) {
            getLog().info("Update complete. Next update will be in: {}", SharedUtils.toTimeFormat(getTimeout()));
        }
    }

    private ServerResponse processAntivirus(ServerResponse resp) {
        final var nodeType = ServerNodeType.ANTIVIRUS;

        // zkusí koupit další node
        resp = tryBuyNode(resp, nodeType);
        var freePieces = getFreePieces(resp, nodeType);

        // jsou k dispozici nějaké AV nody a jsou prostředky
        if (freePieces > 0) {
            if (getNodes(resp, nodeType) > 0) {

                // první node
                if (resp.getAv1Pieces() < updateLimit) {
                    var left = updateLimit - resp.getAv1Pieces();
                    var x5Left = left / 5;

                    for (var i = 0; i < x5Left && freePieces >= 5; i++) {
                        sleep();
                        resp = serverModule.updateNode5x(nodeType, 1);
                        freePieces = getFreePieces(resp, nodeType);
                        left = updateLimit - resp.getAv1Pieces();
                        getLog().info("{} 1 was upgraded 5x to {}. Remaining pieces: {}", nodeType.getAlias(), resp
                                .getAv1Pieces(), resp.getAvFreePieces());
                    }
                    for (var i = 0; i < left && freePieces > 0; i++) {
                        sleep();
                        resp = serverModule.updateNode1x(nodeType, 1);
                        freePieces = getFreePieces(resp, nodeType);
                        getLog().info("{} 1 was upgraded 1x to {}. Remaining pieces: {}", nodeType.getAlias(), resp
                                .getAv1Pieces(), resp.getAvFreePieces());
                    }
                }
                resp = tryBuyNode(resp, nodeType);
                freePieces = getFreePieces(resp, nodeType);

                // druhý node
                if (freePieces > 0 && getNodes(resp, nodeType) > 1 && resp.getAv2Pieces() < updateLimit) {
                    var left = updateLimit - resp.getAv2Pieces();
                    var x5Left = left / 5;

                    for (var i = 0; i < x5Left && freePieces >= 5; i++) {
                        sleep();
                        resp = serverModule.updateNode5x(nodeType, 2);
                        freePieces = getFreePieces(resp, nodeType);
                        left = updateLimit - resp.getAv2Pieces();
                        getLog().info("{} 2 was upgraded 5x to {}. Remaining pieces: {}", nodeType.getAlias(), resp
                                .getAv2Pieces(), resp.getAvFreePieces());
                    }
                    for (var i = 0; i < left && freePieces > 0; i++) {
                        sleep();
                        resp = serverModule.updateNode1x(nodeType, 2);
                        freePieces = getFreePieces(resp, nodeType);
                        getLog().info("{} 2 was upgraded 1x to {}. Remaining pieces: {}", nodeType.getAlias(), resp
                                .getAv2Pieces(), resp.getAvFreePieces());
                    }
                }
                resp = tryBuyNode(resp, nodeType);
                freePieces = getFreePieces(resp, nodeType);

                // třetí node
                if (freePieces > 0 && getNodes(resp, nodeType) > 2 && resp.getAv3Pieces() < updateLimit) {
                    var left = updateLimit - resp.getAv3Pieces();
                    var x5Left = left / 5;

                    for (var i = 0; i < x5Left && freePieces >= 5; i++) {
                        sleep();
                        resp = serverModule.updateNode5x(nodeType, 3);
                        freePieces = getFreePieces(resp, nodeType);
                        left = updateLimit - resp.getAv3Pieces();
                        getLog().info("{} 3 was upgraded 5x to {}. Remaining pieces: {}", nodeType.getAlias(), resp
                                .getAv3Pieces(), resp.getAvFreePieces());
                    }
                    for (var i = 0; i < left && freePieces > 0; i++) {
                        sleep();
                        resp = serverModule.updateNode1x(nodeType, 3);
                        freePieces = getFreePieces(resp, nodeType);
                        getLog().info("{} 3 was upgraded 1x to {}. Remaining pieces: {}", nodeType.getAlias(), resp
                                .getAv3Pieces(), resp.getAvFreePieces());
                    }
                }
            }
        }
        return resp;
    }

    private ServerResponse processFirewall(ServerResponse resp) {
        final var nodeType = ServerNodeType.FIREWALL;

        // zkusí koupit další node
        resp = tryBuyNode(resp, nodeType);
        var freePieces = getFreePieces(resp, nodeType);

        // jsou k dispozici nějaké FW nody a jsou prostředky
        if (freePieces > 0) {
            if (getNodes(resp, nodeType) > 0) {

                // první node
                if (resp.getFw1Pieces() < updateLimit) {
                    var left = updateLimit - resp.getFw1Pieces();
                    var x5Left = left / 5;

                    for (var i = 0; i < x5Left && freePieces >= 5; i++) {
                        sleep();
                        resp = serverModule.updateNode5x(nodeType, 1);
                        freePieces = getFreePieces(resp, nodeType);
                        left = updateLimit - resp.getFw1Pieces();
                        getLog().info("{} 1 was upgraded 5x to {}. Remaining pieces: {}", nodeType.getAlias(), resp
                                .getFw1Pieces(), resp.getFwFreePieces());
                    }
                    for (var i = 0; i < left && freePieces > 0; i++) {
                        sleep();
                        resp = serverModule.updateNode1x(nodeType, 1);
                        freePieces = getFreePieces(resp, nodeType);
                        getLog().info("{} 1 was upgraded 1x to {}. Remaining pieces: {}", nodeType.getAlias(), resp
                                .getFw1Pieces(), resp.getFwFreePieces());
                    }
                }
                resp = tryBuyNode(resp, nodeType);
                freePieces = getFreePieces(resp, nodeType);

                // druhý node
                if (freePieces > 0 && getNodes(resp, nodeType) > 1 && resp.getFw2Pieces() < updateLimit) {
                    var left = updateLimit - resp.getFw2Pieces();
                    var x5Left = left / 5;

                    for (var i = 0; i < x5Left && freePieces >= 5; i++) {
                        sleep();
                        resp = serverModule.updateNode5x(nodeType, 2);
                        freePieces = getFreePieces(resp, nodeType);
                        left = updateLimit - resp.getFw2Pieces();
                        getLog().info("{} 2 was upgraded 5x to {}. Remaining pieces: {}", nodeType.getAlias(), resp
                                .getFw2Pieces(), resp.getFwFreePieces());
                    }
                    for (var i = 0; i < left && freePieces > 0; i++) {
                        sleep();
                        resp = serverModule.updateNode1x(nodeType, 2);
                        freePieces = getFreePieces(resp, nodeType);
                        getLog().info("{} 2 was upgraded 1x to {}. Remaining pieces: {}", nodeType.getAlias(), resp
                                .getFw2Pieces(), resp.getFwFreePieces());
                    }
                }
                resp = tryBuyNode(resp, nodeType);
                freePieces = getFreePieces(resp, nodeType);

                // třetí node
                if (freePieces > 0 && getNodes(resp, nodeType) > 2 && resp.getFw3Pieces() < updateLimit) {
                    var left = updateLimit - resp.getFw3Pieces();
                    var x5Left = left / 5;

                    for (var i = 0; i < x5Left && freePieces >= 5; i++) {
                        sleep();
                        resp = serverModule.updateNode5x(nodeType, 3);
                        freePieces = getFreePieces(resp, nodeType);
                        left = updateLimit - resp.getFw3Pieces();
                        getLog().info("{} 3 was upgraded 5x to {}. Remaining pieces: {}", nodeType.getAlias(), resp
                                .getFw3Pieces(), resp.getFwFreePieces());
                    }
                    for (var i = 0; i < left && freePieces > 0; i++) {
                        sleep();
                        resp = serverModule.updateNode1x(nodeType, 3);
                        freePieces = getFreePieces(resp, nodeType);
                        getLog().info("{} 3 was upgraded 1x to {}. Remaining pieces: {}", nodeType.getAlias(), resp
                                .getFw3Pieces(), resp.getFwFreePieces());
                    }
                }
            }
        }
        return resp;
    }

    private ServerResponse processServer(ServerResponse resp) {
        final var nodeType = ServerNodeType.SERVER;
        var freePieces = getFreePieces(resp, nodeType);

        if (freePieces > 0 && resp.getServerPieces() < coreUpdateLimit) {
            var left = coreUpdateLimit - resp.getServerPieces();
            var x5Left = left / 5;

            for (var i = 0; i < x5Left && freePieces >= 5; i++) {
                sleep();
                resp = serverModule.updateNode5x(nodeType, 1);
                freePieces = getFreePieces(resp, nodeType);
                left = coreUpdateLimit - resp.getServerPieces();
                getLog().info("{} was upgraded 5x to {}. Remaining pieces: {}", nodeType.getAlias(), resp
                        .getServerPieces(), resp.getServerFreePieces());
            }
            for (var i = 0; i < left && freePieces > 0; i++) {
                sleep();
                resp = serverModule.updateNode1x(nodeType, 1);
                freePieces = getFreePieces(resp, nodeType);
                getLog().info("{} was upgraded 1x to {}. Remaining pieces: {}", nodeType.getAlias(), resp
                        .getServerPieces(), resp.getServerFreePieces());
            }
        }
        return resp;
    }

    private ServerResponse processCollectPackages(ServerResponse resp) {
        final var packs = resp.getPacks();

        if (packs > 0) {
            sleep();
            resp = serverModule.openAllPackages();
            getLog().info("All packages ({}) were collected", packs);
        }
        return resp;
    }

    private ServerResponse processBuyAndCollectPackages(ServerResponse resp) {
        if (getConfig().isServerBuyPackagesForNetcoins()) {
            if (resp.getPacksBought() < MAX_BOUGHT_PACKAGES) {
                var netcoins = getShared().getUpdateResponse().getNetCoins();
                var left = MAX_BOUGHT_PACKAGES - resp.getPacksBought();
                var x10Left = left / 10;

                for (var i = 0; i < x10Left && hasEnoughNetcoins(netcoins); i++) {
                    sleep();
                    resp = serverModule.buyPackages(10);
                    left = resp.getBoughtLimit() - resp.getPacksBought();
                    netcoins -= (PACKAGE_COST * 10);
                    getLog().info("Bought 10 packages for {} netcoins. There are {} netcoins left", PACKAGE_COST * 10, netcoins);
                    sleep();
                    resp = serverModule.openAllPackages();
                    getLog().info("Purchased packages were opened");
                }
                for (var i = 0; i < left && hasEnoughNetcoins(netcoins); i++) {
                    sleep();
                    serverModule.buyPackages(1);
                    netcoins -= PACKAGE_COST;
                    getLog().info("Bought 1 package for {} netcoins. There are {} netcoins left", PACKAGE_COST, netcoins);
                    sleep();
                    resp = serverModule.openAllPackages();
                    getLog().info("Purchased packages were opened");
                }
            }
        }
        return resp;
    }

    private ServerResponse tryBuyNode(ServerResponse resp, ServerNodeType nodeType) {
        var freePieces = getFreePieces(resp, nodeType);
        var nodes = getNodes(resp, nodeType);

        if (nodes < MAX_NODES && freePieces >= NEW_NODE_COST) {
            if (nodeType == ServerNodeType.ANTIVIRUS) {
                sleep();
                resp = serverModule.addAntivirusNode();
                getLog().info("Purchased {} {} node", getNodes(resp, nodeType), nodeType.getAlias());

            } else if (nodeType == ServerNodeType.FIREWALL) {
                sleep();
                resp = serverModule.addFirewallNode();
                getLog().info("Purchased {} {} node", getNodes(resp, nodeType), nodeType.getAlias());
            }
        }
        return resp;
    }

    private static int getFreePieces(ServerResponse resp, ServerNodeType nodeType) {
        switch (nodeType) {
            case ANTIVIRUS:
                return resp.getAvFreePieces();
            case FIREWALL:
                return resp.getFwFreePieces();
            case SERVER:
                return resp.getServerFreePieces();
        }
        return 0;
    }

    private static int getNodes(ServerResponse resp, ServerNodeType nodeType) {
        switch (nodeType) {
            case ANTIVIRUS:
                return resp.getAvNodes();
            case FIREWALL:
                return resp.getFwNodes();
            case SERVER:
                return 1;
        }
        return 0;
    }
}
