package cz.ophite.mimic.vhackos.botnet.api.net.response.data;

import cz.ophite.mimic.vhackos.botnet.api.net.response.base.ResponseKey;
import cz.ophite.mimic.vhackos.botnet.shared.ascii.AsciiRow;
import cz.ophite.mimic.vhackos.botnet.shared.ascii.converter.AsciiCrewPositionTypeConverter;
import cz.ophite.mimic.vhackos.botnet.shared.ascii.converter.AsciiElapsedSecondsTimeConverter;

/**
 * Informace o členu v crew.
 *
 * @author mimic
 */
public final class CrewMemberData {

    @AsciiRow("User ID")
    @ResponseKey(KEY_USER_ID)
    private Long userId;
    public static final String P_USER_ID = "userId";
    private static final String KEY_USER_ID = "user_id";

    @AsciiRow(value = "Position", converter = AsciiCrewPositionTypeConverter.class)
    @ResponseKey(KEY_POSITION)
    private Integer position;
    public static final String P_POSITION = "position";
    private static final String KEY_POSITION = "position";

    @AsciiRow("User")
    @ResponseKey(KEY_USER_NAME)
    private String userName;
    public static final String P_USER_NAME = "userName";
    private static final String KEY_USER_NAME = "username";

    @AsciiRow("Level")
    @ResponseKey(KEY_LEVEL)
    private Integer level;
    public static final String P_LEVEL = "level";
    private static final String KEY_LEVEL = "level";

    @AsciiRow(value = "Last Online", converter = AsciiElapsedSecondsTimeConverter.class)
    @ResponseKey(KEY_LAST_ONLINE)
    private Long lastOnline;
    public static final String P_LAST_ONLINE = "lastOnline";
    private static final String KEY_LAST_ONLINE = "lastOnline";

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Long getLastOnline() {
        return lastOnline;
    }

    public void setLastOnline(Long lastOnline) {
        this.lastOnline = lastOnline;
    }
}
