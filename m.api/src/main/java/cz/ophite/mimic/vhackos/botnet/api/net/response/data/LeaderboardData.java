package cz.ophite.mimic.vhackos.botnet.api.net.response.data;

import cz.ophite.mimic.vhackos.botnet.api.net.response.base.ResponseKey;
import cz.ophite.mimic.vhackos.botnet.shared.ascii.AsciiRow;

/**
 * Informace o jednotlivé položce v leaderboards.
 *
 * @author mimic
 */
public final class LeaderboardData {

    @AsciiRow("User")
    @ResponseKey(KEY_USER)
    private String user;
    public static final String P_USER = "user";
    private static final String KEY_USER = "user";

    @AsciiRow("Experience")
    @ResponseKey(KEY_EXP_PERCENT)
    private String expPercent;
    public static final String P_EXP_PERCENT = "expPercent";
    private static final String KEY_EXP_PERCENT = "exp";

    @AsciiRow("Level")
    @ResponseKey(KEY_LEVEL)
    private Integer level;
    public static final String P_LEVEL = "level";
    private static final String KEY_LEVEL = "level";

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getExpPercent() {
        return expPercent;
    }

    public void setExpPercent(String expPercent) {
        this.expPercent = expPercent;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }
}
