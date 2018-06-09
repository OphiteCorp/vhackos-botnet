package cz.ophite.mimic.vhackos.botnet.api.net.response.data;

import cz.ophite.mimic.vhackos.botnet.api.net.response.base.ResponseKey;
import cz.ophite.mimic.vhackos.botnet.shared.utils.ascii.AsciiRow;
import cz.ophite.mimic.vhackos.botnet.shared.utils.ascii.converter.AsciiBruteStateConverter;

/**
 * Informace o IP, která je na seznamu "brute" IP mezi tásky.
 *
 * @author mimic
 */
public final class IpBruteDetailData {

    @AsciiRow("Brute ID")
    @ResponseKey(KEY_BRUTE_ID)
    private Long bruteId;
    public static final String P_BRUTE_ID = "bruteId";
    private static final String KEY_BRUTE_ID = "id";

    @AsciiRow("IP")
    @ResponseKey(KEY_IP)
    private String ip;
    public static final String P_IP = "ip";
    private static final String KEY_IP = "user_ip";

    @AsciiRow("Start")
    @ResponseKey(KEY_START_TIME)
    private Long startTime;
    public static final String P_START_TIME = "startTime";
    private static final String KEY_START_TIME = "start";

    @AsciiRow("End")
    @ResponseKey(KEY_END_TIME)
    private Long endTime;
    public static final String P_END_TIME = "endTime";
    private static final String KEY_END_TIME = "end";

    @AsciiRow("Current Time")
    @ResponseKey(KEY_CURRENT_TIME)
    private Long currentTime;
    public static final String P_CURRENT_TIME = "currentTime";
    private static final String KEY_CURRENT_TIME = "now";

    @AsciiRow("User")
    @ResponseKey(KEY_USER_NAME)
    private String userName;
    public static final String P_USER_NAME = "userName";
    private static final String KEY_USER_NAME = "username";

    @AsciiRow(value = "State", converter = AsciiBruteStateConverter.class)
    @ResponseKey(KEY_RESULT)
    private Integer result;
    public static final String P_RESULT = "result";
    private static final String KEY_RESULT = "result";

    public Long getBruteId() {
        return bruteId;
    }

    public void setBruteId(Long bruteId) {
        this.bruteId = bruteId;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    public Long getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(Long currentTime) {
        this.currentTime = currentTime;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getResult() {
        return result;
    }

    public void setResult(Integer result) {
        this.result = result;
    }
}
