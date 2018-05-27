package cz.ophite.mimic.vhackos.botnet.api.net.response;

import cz.ophite.mimic.vhackos.botnet.api.net.response.base.Response;
import cz.ophite.mimic.vhackos.botnet.api.net.response.base.ResponseKey;
import cz.ophite.mimic.vhackos.botnet.shared.utils.ascii.AsciiRow;
import cz.ophite.mimic.vhackos.botnet.shared.utils.ascii.converter.AsciiBooleanConverter;
import cz.ophite.mimic.vhackos.botnet.shared.utils.ascii.converter.AsciiElapsedSecondsTimeConverter;
import cz.ophite.mimic.vhackos.botnet.shared.utils.ascii.converter.AsciiServerNodeTypeConverter;

/**
 * Informace o serveru.
 *
 * @author mimic
 */
public final class ServerResponse extends Response {

    public static final int MAX_BOUGHT_PACKS = 50;
    public static final int MAX_PACKS = 10;

    @AsciiRow(value = "AV Added", converter = AsciiBooleanConverter.class)
    @ResponseKey("avadded")
    private Integer avAdded;
    public static final String P_AV_ADDED = "avAdded";

    @AsciiRow(value = "FW Added", converter = AsciiBooleanConverter.class)
    @ResponseKey("fwadded")
    private Integer fwAdded;
    public static final String P_FW_ADDED = "fwAdded";

    @AsciiRow(value = "Node Updated", converter = AsciiBooleanConverter.class)
    @ResponseKey("node_updated")
    private Integer nodeUpdated;
    public static final String P_NODE_UPDATED = "nodeUpdated";

    @AsciiRow(value = "sPack Open", converter = AsciiBooleanConverter.class)
    @ResponseKey("sPackOpen")
    private Integer sPackOpen;
    public static final String P_SPACK_OPEN = "sPackOpen";

    @AsciiRow(value = "sPack Open All", converter = AsciiBooleanConverter.class)
    @ResponseKey("sPackOpenAll")
    private Integer sPackOpenAll;
    public static final String P_SPACK_OPEN_ALL = "sPackOpenAll";

    @AsciiRow(value = "Bought One", converter = AsciiBooleanConverter.class)
    @ResponseKey("boughtOne")
    private Integer boughtOne;
    public static final String P_BOUGHT_ONE = "boughtOne";

    @AsciiRow(value = "Bought Ten", converter = AsciiBooleanConverter.class)
    @ResponseKey("boughtTen")
    private Integer boughtTen;
    public static final String P_BOUGHT_TEN = "boughtTen";

    @AsciiRow("Packs Bought")
    @ResponseKey("packsBought")
    private Integer packsBought;
    public static final String P_PACKS_BOUGHT = "packsBought";

    @AsciiRow("Bought Limit")
    @ResponseKey("boughtLimit")
    private Integer boughtLimit;
    public static final String P_BOUGHT_LIMIT = "boughtLimit";

    @AsciiRow("Server Pieces")
    @ResponseKey("server_str")
    private Integer serverPieces;
    public static final String P_SERVER_PIECES = "serverPieces";

    @AsciiRow("AV1 Pieces")
    @ResponseKey("av1_str")
    private Integer av1Pieces;
    public static final String P_AV1_PIECES = "av1Pieces";

    @AsciiRow("AV2 Pieces")
    @ResponseKey("av2_str")
    private Integer av2Pieces;
    public static final String P_AV2_PIECES = "av2Pieces";

    @AsciiRow("AV3 Pieces")
    @ResponseKey("av3_str")
    private Integer av3Pieces;
    public static final String P_AV3_PIECES = "av3Pieces";

    @AsciiRow("FW1 Pieces")
    @ResponseKey("fw1_str")
    private Integer fw1Pieces;
    public static final String P_FW1_PIECES = "fw1Pieces";

    @AsciiRow("FW2 Pieces")
    @ResponseKey("fw2_str")
    private Integer fw2Pieces;
    public static final String P_FW2_PIECES = "fw2Pieces";

    @AsciiRow("FW3 Pieces")
    @ResponseKey("fw3_str")
    private Integer fw3Pieces;
    public static final String P_FW3_PIECES = "fw3Pieces";

    @AsciiRow("Server Free Pieces")
    @ResponseKey("server_pieces")
    private Integer serverFreePieces;
    public static final String P_SERVER_FREE_PIECES = "serverFreePieces";

    @AsciiRow("AV Free Pieces")
    @ResponseKey("av_pieces")
    private Integer avFreePieces;
    public static final String P_AV_FREE_PIECES = "avFreePieces";

    @AsciiRow("FW Free Pieces")
    @ResponseKey("fw_pieces")
    private Integer fwFreePieces;
    public static final String P_FW_FREE_PIECES = "fwFreePieces";

    @AsciiRow("Packs")
    @ResponseKey("packs")
    private Integer packs;
    public static final String P_PACKS = "packs";

    @AsciiRow("Server Max Pieces")
    @ResponseKey("server_str_max")
    private Integer serverMaxPieces;
    public static final String P_SERVER_MAX_PIECES = "serverMaxPieces";

    @AsciiRow("Server Stars")
    @ResponseKey("server_str_stars")
    private Integer serverStars;
    public static final String P_SERVER_STARS = "serverStars";

    @AsciiRow("AV1 Max Pieces")
    @ResponseKey("av1_str_max")
    private Integer av1MaxPieces;
    public static final String P_AV1_MAX_PIECES = "av1MaxPieces";

    @AsciiRow("AV1 Stars")
    @ResponseKey("av1_str_stars")
    private Integer av1Stars;
    public static final String P_AV1_STARS = "av1Stars";

    @AsciiRow("AV2 Max Pieces")
    @ResponseKey("av2_str_max")
    private Integer av2MaxPieces;
    public static final String P_AV2_MAX_PIECES = "av2MaxPieces";

    @AsciiRow("AV2 Stars")
    @ResponseKey("av2_str_stars")
    private Integer av2Stars;
    public static final String P_AV2_STARS = "av2Stars";

    @AsciiRow("AV3 Max Pieces")
    @ResponseKey("av3_str_max")
    private Integer av3MaxPieces;
    public static final String P_AV3_MAX_PIECES = "av3MaxPieces";

    @AsciiRow("AV3 Stars")
    @ResponseKey("av3_str_stars")
    private Integer av3Stars;
    public static final String P_AV3_STARS = "av3Stars";

    @AsciiRow("FW1 Max Pieces")
    @ResponseKey("fw1_str_max")
    private Integer fw1MaxPieces;
    public static final String P_FW1_MAX_PIECES = "fw1MaxPieces";

    @AsciiRow("FW1 Stars")
    @ResponseKey("fw1_str_stars")
    private Integer fw1Stars;
    public static final String P_FW1_STARS = "fw1Stars";

    @AsciiRow("FW2 Max Pieces")
    @ResponseKey("fw2_str_max")
    private Integer fw2MaxPieces;
    public static final String P_FW2_MAX_PIECES = "fw2MaxPieces";

    @AsciiRow("FW2 Stars")
    @ResponseKey("fw2_str_stars")
    private Integer fw2Stars;
    public static final String P_FW2_STARS = "fw2Stars";

    @AsciiRow("FW3 Max Pieces")
    @ResponseKey("fw3_str_max")
    private Integer fw3MaxPieces;
    public static final String P_FW3_MAX_PIECES = "fw3MaxPieces";

    @AsciiRow("FW3 Stars")
    @ResponseKey("fw3_str_stars")
    private Integer fw3Stars;
    public static final String P_FW3_STARS = "fw3Stars";

    @AsciiRow("AV Nodes")
    @ResponseKey("avnodes")
    private Integer avNodes;
    public static final String P_AV_NODES = "avNodes";

    @AsciiRow("FW Nodes")
    @ResponseKey("fwnodes")
    private Integer fwNodes;
    public static final String P_FW_NODES = "fwNodes";

    @AsciiRow(value = "Next Pack In", converter = AsciiElapsedSecondsTimeConverter.class)
    @ResponseKey("nextpackin")
    private Long nextPackIn;
    public static final String P_NEXT_PACK_IN = "nextPackIn";

    @AsciiRow(value = "Node Type", converter = AsciiServerNodeTypeConverter.class)
    @ResponseKey("node_type")
    private Integer nodeType;
    public static final String P_NODE_TYPE = "nodeType";

    @AsciiRow("Node Number")
    @ResponseKey("node_number")
    private Integer nodeNumber;
    public static final String P_NODE_NUMBER = "nodeNumber";

    @AsciiRow("sPack Type")
    @ResponseKey("sPackType")
    private Integer sPackType;
    public static final String P_SPACK_TYPE = "sPackType";

    @AsciiRow("sHow Much")
    @ResponseKey("sHowMuch")
    private Integer sHowMuch;
    public static final String P_SHOW_MUCH = "sHowMuch";

    public Integer getAvAdded() {
        return avAdded;
    }

    public void setAvAdded(Integer avAdded) {
        this.avAdded = avAdded;
    }

    public Integer getFwAdded() {
        return fwAdded;
    }

    public void setFwAdded(Integer fwAdded) {
        this.fwAdded = fwAdded;
    }

    public Integer getNodeUpdated() {
        return nodeUpdated;
    }

    public void setNodeUpdated(Integer nodeUpdated) {
        this.nodeUpdated = nodeUpdated;
    }

    public Integer getsPackOpen() {
        return sPackOpen;
    }

    public void setsPackOpen(Integer sPackOpen) {
        this.sPackOpen = sPackOpen;
    }

    public Integer getsPackOpenAll() {
        return sPackOpenAll;
    }

    public void setsPackOpenAll(Integer sPackOpenAll) {
        this.sPackOpenAll = sPackOpenAll;
    }

    public Integer getBoughtOne() {
        return boughtOne;
    }

    public void setBoughtOne(Integer boughtOne) {
        this.boughtOne = boughtOne;
    }

    public Integer getBoughtTen() {
        return boughtTen;
    }

    public void setBoughtTen(Integer boughtTen) {
        this.boughtTen = boughtTen;
    }

    public Integer getPacksBought() {
        return packsBought;
    }

    public void setPacksBought(Integer packsBought) {
        this.packsBought = packsBought;
    }

    public Integer getBoughtLimit() {
        return boughtLimit;
    }

    public void setBoughtLimit(Integer boughtLimit) {
        this.boughtLimit = boughtLimit;
    }

    public Integer getServerPieces() {
        return serverPieces;
    }

    public void setServerPieces(Integer serverPieces) {
        this.serverPieces = serverPieces;
    }

    public Integer getAv1Pieces() {
        return av1Pieces;
    }

    public void setAv1Pieces(Integer av1Pieces) {
        this.av1Pieces = av1Pieces;
    }

    public Integer getAv2Pieces() {
        return av2Pieces;
    }

    public void setAv2Pieces(Integer av2Pieces) {
        this.av2Pieces = av2Pieces;
    }

    public Integer getAv3Pieces() {
        return av3Pieces;
    }

    public void setAv3Pieces(Integer av3Pieces) {
        this.av3Pieces = av3Pieces;
    }

    public Integer getFw1Pieces() {
        return fw1Pieces;
    }

    public void setFw1Pieces(Integer fw1Pieces) {
        this.fw1Pieces = fw1Pieces;
    }

    public Integer getFw2Pieces() {
        return fw2Pieces;
    }

    public void setFw2Pieces(Integer fw2Pieces) {
        this.fw2Pieces = fw2Pieces;
    }

    public Integer getFw3Pieces() {
        return fw3Pieces;
    }

    public void setFw3Pieces(Integer fw3Pieces) {
        this.fw3Pieces = fw3Pieces;
    }

    public Integer getServerFreePieces() {
        return serverFreePieces;
    }

    public void setServerFreePieces(Integer serverFreePieces) {
        this.serverFreePieces = serverFreePieces;
    }

    public Integer getAvFreePieces() {
        return avFreePieces;
    }

    public void setAvFreePieces(Integer avFreePieces) {
        this.avFreePieces = avFreePieces;
    }

    public Integer getFwFreePieces() {
        return fwFreePieces;
    }

    public void setFwFreePieces(Integer fwFreePieces) {
        this.fwFreePieces = fwFreePieces;
    }

    public Integer getPacks() {
        return packs;
    }

    public void setPacks(Integer packs) {
        this.packs = packs;
    }

    public Integer getServerMaxPieces() {
        return serverMaxPieces;
    }

    public void setServerMaxPieces(Integer serverMaxPieces) {
        this.serverMaxPieces = serverMaxPieces;
    }

    public Integer getServerStars() {
        return serverStars;
    }

    public void setServerStars(Integer serverStars) {
        this.serverStars = serverStars;
    }

    public Integer getAv1MaxPieces() {
        return av1MaxPieces;
    }

    public void setAv1MaxPieces(Integer av1MaxPieces) {
        this.av1MaxPieces = av1MaxPieces;
    }

    public Integer getAv1Stars() {
        return av1Stars;
    }

    public void setAv1Stars(Integer av1Stars) {
        this.av1Stars = av1Stars;
    }

    public Integer getAv2MaxPieces() {
        return av2MaxPieces;
    }

    public void setAv2MaxPieces(Integer av2MaxPieces) {
        this.av2MaxPieces = av2MaxPieces;
    }

    public Integer getAv2Stars() {
        return av2Stars;
    }

    public void setAv2Stars(Integer av2Stars) {
        this.av2Stars = av2Stars;
    }

    public Integer getAv3MaxPieces() {
        return av3MaxPieces;
    }

    public void setAv3MaxPieces(Integer av3MaxPieces) {
        this.av3MaxPieces = av3MaxPieces;
    }

    public Integer getAv3Stars() {
        return av3Stars;
    }

    public void setAv3Stars(Integer av3Stars) {
        this.av3Stars = av3Stars;
    }

    public Integer getFw1MaxPieces() {
        return fw1MaxPieces;
    }

    public void setFw1MaxPieces(Integer fw1MaxPieces) {
        this.fw1MaxPieces = fw1MaxPieces;
    }

    public Integer getFw1Stars() {
        return fw1Stars;
    }

    public void setFw1Stars(Integer fw1Stars) {
        this.fw1Stars = fw1Stars;
    }

    public Integer getFw2MaxPieces() {
        return fw2MaxPieces;
    }

    public void setFw2MaxPieces(Integer fw2MaxPieces) {
        this.fw2MaxPieces = fw2MaxPieces;
    }

    public Integer getFw2Stars() {
        return fw2Stars;
    }

    public void setFw2Stars(Integer fw2Stars) {
        this.fw2Stars = fw2Stars;
    }

    public Integer getFw3MaxPieces() {
        return fw3MaxPieces;
    }

    public void setFw3MaxPieces(Integer fw3MaxPieces) {
        this.fw3MaxPieces = fw3MaxPieces;
    }

    public Integer getFw3Stars() {
        return fw3Stars;
    }

    public void setFw3Stars(Integer fw3Stars) {
        this.fw3Stars = fw3Stars;
    }

    public Integer getAvNodes() {
        return avNodes;
    }

    public void setAvNodes(Integer avNodes) {
        this.avNodes = avNodes;
    }

    public Integer getFwNodes() {
        return fwNodes;
    }

    public void setFwNodes(Integer fwNodes) {
        this.fwNodes = fwNodes;
    }

    public Long getNextPackIn() {
        return nextPackIn;
    }

    public void setNextPackIn(Long nextPackIn) {
        this.nextPackIn = nextPackIn;
    }

    public Integer getNodeType() {
        return nodeType;
    }

    public void setNodeType(Integer nodeType) {
        this.nodeType = nodeType;
    }

    public Integer getNodeNumber() {
        return nodeNumber;
    }

    public void setNodeNumber(Integer nodeNumber) {
        this.nodeNumber = nodeNumber;
    }

    public Integer getsPackType() {
        return sPackType;
    }

    public void setsPackType(Integer sPackType) {
        this.sPackType = sPackType;
    }

    public Integer getsHowMuch() {
        return sHowMuch;
    }

    public void setsHowMuch(Integer sHowMuch) {
        this.sHowMuch = sHowMuch;
    }
}
