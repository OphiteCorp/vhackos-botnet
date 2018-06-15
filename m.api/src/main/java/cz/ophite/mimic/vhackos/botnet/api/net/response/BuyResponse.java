package cz.ophite.mimic.vhackos.botnet.api.net.response;

import cz.ophite.mimic.vhackos.botnet.api.net.response.base.Response;
import cz.ophite.mimic.vhackos.botnet.api.net.response.base.ResponseKey;
import cz.ophite.mimic.vhackos.botnet.api.net.response.data.BuyItemData;
import cz.ophite.mimic.vhackos.botnet.shared.ascii.AsciiRow;
import cz.ophite.mimic.vhackos.botnet.shared.ascii.converter.AsciiBooleanConverter;

import java.util.List;

/**
 * Informace o obchodu za peníze nebo netcoins.
 *
 * @author mimic
 */
public final class BuyResponse extends Response {

    @AsciiRow(value = "Inet Upgraded", converter = AsciiBooleanConverter.class)
    @ResponseKey("inetupgraded")
    private Integer inetUpgraded;
    public static final String P_INET_UPGRADED = "inetUpgraded";

    @AsciiRow(value = "IP Change", converter = AsciiBooleanConverter.class)
    @ResponseKey("ipchange")
    private Integer ipChange;
    public static final String P_IP_CHANGE = "ipChange";

    @AsciiRow(value = "Bought", converter = AsciiBooleanConverter.class)
    @ResponseKey("bought")
    private Integer bought;
    public static final String P_BOUGHT = "bought";

    @AsciiRow("Purchase Limit")
    @ResponseKey("purchaselimit")
    private Integer purchaseLimit;
    public static final String P_PURCHASE_LIMIT = "purchaseLimit";

    @AsciiRow("Inet")
    @ResponseKey("inet")
    private Integer inet;
    public static final String P_INET = "inet";

    @AsciiRow("Inet Costs")
    @ResponseKey("inetcosts")
    private Integer inetCosts;
    public static final String P_INET_COSTS = "inetCosts";

    @AsciiRow("Required Level Inet")
    @ResponseKey("reqlevelinet")
    private Integer reqLevelInet;
    public static final String P_REQ_LEVEL_INET = "reqLevelInet";

    @AsciiRow("Next Inet")
    @ResponseKey("nextinet")
    private String nextInet;
    public static final String P_NEXT_INET = "nextInet";

    @AsciiRow("Can")
    @ResponseKey("can")
    private Integer can;
    public static final String P_CAN = "can";

    @AsciiRow("Can Max")
    @ResponseKey("canmax")
    private Integer canMax;
    public static final String P_CAN_MAX = "canMax";

    @AsciiRow("SKU Count")
    @ResponseKey("SKUCount")
    private Integer SKUCount;
    public static final String P_SKU_COUNT = "SKUCount";

    @AsciiRow("SKUs")
    @ResponseKey("SKU")
    private List<String> SKUs;
    public static final String P_SKUS = "SKUs";

    @AsciiRow("Items")
    @ResponseKey("item")
    private List<BuyItemData> items;
    public static final String P_ITEMS = "items";

    public Integer getInetUpgraded() {
        return inetUpgraded;
    }

    public void setInetUpgraded(Integer inetUpgraded) {
        this.inetUpgraded = inetUpgraded;
    }

    public Integer getIpChange() {
        return ipChange;
    }

    public void setIpChange(Integer ipChange) {
        this.ipChange = ipChange;
    }

    public Integer getBought() {
        return bought;
    }

    public void setBought(Integer bought) {
        this.bought = bought;
    }

    public Integer getPurchaseLimit() {
        return purchaseLimit;
    }

    public void setPurchaseLimit(Integer purchaseLimit) {
        this.purchaseLimit = purchaseLimit;
    }

    public Integer getInet() {
        return inet;
    }

    public void setInet(Integer inet) {
        this.inet = inet;
    }

    public Integer getInetCosts() {
        return inetCosts;
    }

    public void setInetCosts(Integer inetCosts) {
        this.inetCosts = inetCosts;
    }

    public Integer getReqLevelInet() {
        return reqLevelInet;
    }

    public void setReqLevelInet(Integer reqLevelInet) {
        this.reqLevelInet = reqLevelInet;
    }

    public String getNextInet() {
        return nextInet;
    }

    public void setNextInet(String nextInet) {
        this.nextInet = nextInet;
    }

    public Integer getCan() {
        return can;
    }

    public void setCan(Integer can) {
        this.can = can;
    }

    public Integer getCanMax() {
        return canMax;
    }

    public void setCanMax(Integer canMax) {
        this.canMax = canMax;
    }

    public Integer getSKUCount() {
        return SKUCount;
    }

    public void setSKUCount(Integer SKUCount) {
        this.SKUCount = SKUCount;
    }

    public List<String> getSKUs() {
        return SKUs;
    }

    public void setSKUs(List<String> SKUs) {
        this.SKUs = SKUs;
    }

    public List<BuyItemData> getItems() {
        return items;
    }

    public void setItems(List<BuyItemData> items) {
        this.items = items;
    }
}
