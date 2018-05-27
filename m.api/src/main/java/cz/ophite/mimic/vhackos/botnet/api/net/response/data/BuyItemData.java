package cz.ophite.mimic.vhackos.botnet.api.net.response.data;

import cz.ophite.mimic.vhackos.botnet.api.net.response.base.ResponseKey;
import cz.ophite.mimic.vhackos.botnet.shared.utils.ascii.AsciiRow;
import cz.ophite.mimic.vhackos.botnet.shared.utils.ascii.converter.AsciiMoneyConverter;

/**
 * Informace o položce ke koupi za peníze nebo netcoins.
 *
 * @author mimic
 */
public final class BuyItemData {

    @AsciiRow("SKU")
    @ResponseKey(KEY_SKU)
    private String sku;
    public static final String P_SKU = "sku";
    private static final String KEY_SKU = "sku";

    @AsciiRow(value = "Price", converter = AsciiMoneyConverter.class)
    @ResponseKey(KEY_PRICE)
    private Integer price;
    public static final String P_PRICE = "price";
    private static final String KEY_PRICE = "price";

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }
}
