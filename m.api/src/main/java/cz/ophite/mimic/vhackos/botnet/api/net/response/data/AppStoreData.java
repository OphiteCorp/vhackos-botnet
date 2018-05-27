package cz.ophite.mimic.vhackos.botnet.api.net.response.data;

import cz.ophite.mimic.vhackos.botnet.api.net.response.base.ResponseKey;
import cz.ophite.mimic.vhackos.botnet.shared.utils.ascii.AsciiRow;
import cz.ophite.mimic.vhackos.botnet.shared.utils.ascii.converter.AsciiAppStoreTypeConverter;
import cz.ophite.mimic.vhackos.botnet.shared.utils.ascii.converter.AsciiMoneyConverter;

/**
 * Informace o aplikaci v obchodě.
 *
 * @author mimic
 */
public final class AppStoreData {

    @AsciiRow(value = "Price", converter = AsciiMoneyConverter.class)
    @ResponseKey(KEY_PRICE)
    private Integer price;
    public static final String P_PRICE = "price";
    private static final String KEY_PRICE = "price";

    @AsciiRow(value = "Base Price", converter = AsciiMoneyConverter.class)
    @ResponseKey(KEY_BASE_PRICE)
    private Integer basePrice;
    public static final String P_BASE_PRICE = "basePrice";
    private static final String KEY_BASE_PRICE = "baseprice";

    @AsciiRow("Factor")
    @ResponseKey(KEY_FACTOR)
    private Integer factor;
    public static final String P_FACTOR = "factor";
    private static final String KEY_FACTOR = "factor";

    @AsciiRow(value = "ID", converter = AsciiAppStoreTypeConverter.class)
    @ResponseKey(KEY_APP_ID)
    private Integer appId;
    public static final String P_APP_ID = "appId";
    private static final String KEY_APP_ID = "appid";

    @AsciiRow("Level")
    @ResponseKey(KEY_LEVEL)
    private Integer level;
    public static final String P_LEVEL = "level";
    private static final String KEY_LEVEL = "level";

    @AsciiRow("Require Level")
    @ResponseKey(KEY_REQUIRE_LEVEL)
    private Integer requireLevel;
    public static final String P_REQUIRE_LEVEL = "requireLevel";
    private static final String KEY_REQUIRE_LEVEL = "require";

    @AsciiRow("Max Level")
    @ResponseKey(KEY_MAX_LEVEL)
    private Integer maxLevel;
    public static final String P_MAX_LEVEL = "maxLevel";
    private static final String KEY_MAX_LEVEL = "maxlvl";

    @AsciiRow("Running")
    @ResponseKey(KEY_RUNNING)
    private Integer running;
    public static final String P_RUNNING = "running";
    private static final String KEY_RUNNING = "running";

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(Integer basePrice) {
        this.basePrice = basePrice;
    }

    public Integer getFactor() {
        return factor;
    }

    public void setFactor(Integer factor) {
        this.factor = factor;
    }

    public Integer getAppId() {
        return appId;
    }

    public void setAppId(Integer appId) {
        this.appId = appId;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getRequireLevel() {
        return requireLevel;
    }

    public void setRequireLevel(Integer requireLevel) {
        this.requireLevel = requireLevel;
    }

    public Integer getMaxLevel() {
        return maxLevel;
    }

    public void setMaxLevel(Integer maxLevel) {
        this.maxLevel = maxLevel;
    }

    public Integer getRunning() {
        return running;
    }

    public void setRunning(Integer running) {
        this.running = running;
    }
}
