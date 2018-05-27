package cz.ophite.mimic.vhackos.botnet.api.net.response;

import cz.ophite.mimic.vhackos.botnet.api.net.response.base.Response;
import cz.ophite.mimic.vhackos.botnet.api.net.response.base.ResponseKey;
import cz.ophite.mimic.vhackos.botnet.api.net.response.data.AppStoreData;
import cz.ophite.mimic.vhackos.botnet.shared.utils.ascii.AsciiRow;
import cz.ophite.mimic.vhackos.botnet.shared.utils.ascii.converter.AsciiBooleanConverter;
import cz.ophite.mimic.vhackos.botnet.shared.utils.ascii.converter.AsciiMoneyConverter;

import java.util.List;

/**
 * Informace o aplikacích z obchodu.
 *
 * @author mimic
 */
public final class AppStoreResponse extends Response {

    @AsciiRow(value = "Level Up", converter = AsciiBooleanConverter.class)
    @ResponseKey("lvlup")
    private Integer levelUp;
    public static final String P_LEVEL_UP = "levelUp";

    @AsciiRow("Updated")
    @ResponseKey("updated")
    private Integer updated;
    public static final String P_UPDATED = "updated";

    @AsciiRow("Installed")
    @ResponseKey("installed")
    private Integer installed;
    public static final String P_INSTALLED = "installed";

    @AsciiRow("Filled")
    @ResponseKey("filled")
    private Integer filled;
    public static final String P_FILLED = "filled";

    @AsciiRow("Level")
    @ResponseKey("level")
    private Integer level;
    public static final String P_LEVEL = "level";

    @AsciiRow(value = "Money", converter = AsciiMoneyConverter.class)
    @ResponseKey("money")
    private Long money;
    public static final String P_MONEY = "money";

    @AsciiRow("Spam")
    @ResponseKey("spam")
    private Integer spam;
    public static final String P_SPAM = "spam";

    @AsciiRow("App Count")
    @ResponseKey("appCount")
    private Integer appCount;
    public static final String P_APP_COUNT = "appCount";

    @AsciiRow("Apps")
    @ResponseKey("apps")
    private List<AppStoreData> apps;
    public static final String P_APPS = "apps";

    public Integer getLevelUp() {
        return levelUp;
    }

    public void setLevelUp(Integer levelUp) {
        this.levelUp = levelUp;
    }

    public Integer getUpdated() {
        return updated;
    }

    public void setUpdated(Integer updated) {
        this.updated = updated;
    }

    public Integer getInstalled() {
        return installed;
    }

    public void setInstalled(Integer installed) {
        this.installed = installed;
    }

    public Integer getFilled() {
        return filled;
    }

    public void setFilled(Integer filled) {
        this.filled = filled;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Long getMoney() {
        return money;
    }

    public void setMoney(Long money) {
        this.money = money;
    }

    public Integer getSpam() {
        return spam;
    }

    public void setSpam(Integer spam) {
        this.spam = spam;
    }

    public Integer getAppCount() {
        return appCount;
    }

    public void setAppCount(Integer appCount) {
        this.appCount = appCount;
    }

    public List<AppStoreData> getApps() {
        return apps;
    }

    public void setApps(List<AppStoreData> apps) {
        this.apps = apps;
    }
}
