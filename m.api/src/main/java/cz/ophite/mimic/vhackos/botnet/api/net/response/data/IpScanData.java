package cz.ophite.mimic.vhackos.botnet.api.net.response.data;

import cz.ophite.mimic.vhackos.botnet.api.net.response.base.ResponseKey;
import cz.ophite.mimic.vhackos.botnet.shared.ascii.AsciiRow;
import cz.ophite.mimic.vhackos.botnet.shared.ascii.converter.AsciiBooleanConverter;

/**
 * Informace o skenované IP.
 *
 * @author mimic
 */
public final class IpScanData {

    @AsciiRow("IP")
    @ResponseKey(KEY_IP)
    private String ip;
    public static final String P_IP = "ip";
    private static final String KEY_IP = "ip";

    @AsciiRow("Level")
    @ResponseKey(KEY_LEVEL)
    private Integer level;
    public static final String P_LEVEL = "level";
    private static final String KEY_LEVEL = "level";

    @AsciiRow("Firewall")
    @ResponseKey(KEY_FIREWALL)
    private Integer firewall;
    public static final String P_FIREWALL = "firewall";
    private static final String KEY_FIREWALL = "fw";

    @AsciiRow(value = "Open", converter = AsciiBooleanConverter.class)
    @ResponseKey(KEY_OPEN)
    private Integer open;
    public static final String P_OPEN = "open";
    private static final String KEY_OPEN = "open";

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getFirewall() {
        return firewall;
    }

    public void setFirewall(Integer firewall) {
        this.firewall = firewall;
    }

    public Integer getOpen() {
        return open;
    }

    public void setOpen(Integer open) {
        this.open = open;
    }
}
