package cz.ophite.mimic.vhackos.botnet.api.net.response.data;

import cz.ophite.mimic.vhackos.botnet.api.net.response.base.ResponseKey;
import cz.ophite.mimic.vhackos.botnet.shared.ascii.AsciiRow;
import cz.ophite.mimic.vhackos.botnet.shared.ascii.converter.AsciiElapsedTimeConverter;

/**
 * Crew zpráva.
 *
 * @author mimic
 */
public final class CrewMessageData {

    @AsciiRow("User ID")
    @ResponseKey(KEY_USER_ID)
    private Long userId;
    public static final String P_USER_ID = "userId";
    private static final String KEY_USER_ID = "user_id";

    @AsciiRow("Message")
    @ResponseKey(KEY_MESSAGE)
    private String message;
    public static final String P_MESSAGE = "message";
    private static final String KEY_MESSAGE = "message";

    @AsciiRow("User")
    @ResponseKey(KEY_USER_NAME)
    private String userName;
    public static final String P_USER_NAME = "userName";
    private static final String KEY_USER_NAME = "username";

    @AsciiRow(value = "Time", converter = AsciiElapsedTimeConverter.class)
    @ResponseKey(KEY_TIME)
    private Long time;
    public static final String P_TIME = "time";
    private static final String KEY_TIME = "time";

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }
}
