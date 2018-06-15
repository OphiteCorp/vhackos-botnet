package cz.ophite.mimic.vhackos.botnet.api.net.response.data;

import cz.ophite.mimic.vhackos.botnet.api.net.response.base.ResponseKey;
import cz.ophite.mimic.vhackos.botnet.shared.ascii.AsciiRow;
import cz.ophite.mimic.vhackos.botnet.shared.ascii.converter.AsciiBruteStateConverter;
import cz.ophite.mimic.vhackos.botnet.shared.dto.BruteState;

/**
 * Informace o prolomené IP.
 *
 * @author mimic
 */
public final class IpBruteData {

    @AsciiRow("IP")
    @ResponseKey(KEY_IP)
    private String ip;
    public static final String P_IP = "ip";
    private static final String KEY_IP = "ip";

    @AsciiRow("User")
    @ResponseKey(KEY_USER_NAME)
    private String userName;
    public static final String P_USER_NAME = "userName";
    private static final String KEY_USER_NAME = "username";

    @AsciiRow(value = "Brute", converter = AsciiBruteStateConverter.class)
    @ResponseKey(KEY_BRUTE)
    private Integer brute;
    public static final String P_BRUTE = "brute";
    private static final String KEY_BRUTE = "brute";

    public BruteState getBruteState() {
        return BruteState.getbyState(brute);
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getBrute() {
        return brute;
    }

    public void setBrute(Integer brute) {
        this.brute = brute;
    }
}
