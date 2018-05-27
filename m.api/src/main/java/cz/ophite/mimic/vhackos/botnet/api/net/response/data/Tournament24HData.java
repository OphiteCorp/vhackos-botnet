package cz.ophite.mimic.vhackos.botnet.api.net.response.data;

import cz.ophite.mimic.vhackos.botnet.api.net.response.base.ResponseKey;
import cz.ophite.mimic.vhackos.botnet.shared.utils.ascii.AsciiRow;

/**
 * Informace o položce v 24H turnaji v leaderboards.
 *
 * @author mimic
 */
public final class Tournament24HData {

    @AsciiRow("User")
    @ResponseKey(KEY_USER)
    private String user;
    public static final String P_USER = "user";
    private static final String KEY_USER = "user";

    @AsciiRow("Experience Gain")
    @ResponseKey(KEY_EXP_GAIN)
    private Long expGain;
    public static final String P_EXP_GAIN = "expGain";
    private static final String KEY_EXP_GAIN = "expgain";

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

    public Long getExpGain() {
        return expGain;
    }

    public void setExpGain(Long expGain) {
        this.expGain = expGain;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }
}
