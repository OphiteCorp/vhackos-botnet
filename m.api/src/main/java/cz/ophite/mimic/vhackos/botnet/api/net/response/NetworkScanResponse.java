package cz.ophite.mimic.vhackos.botnet.api.net.response;

import cz.ophite.mimic.vhackos.botnet.api.net.response.base.Response;
import cz.ophite.mimic.vhackos.botnet.api.net.response.base.ResponseKey;
import cz.ophite.mimic.vhackos.botnet.api.net.response.data.IpBruteData;
import cz.ophite.mimic.vhackos.botnet.api.net.response.data.IpScanData;
import cz.ophite.mimic.vhackos.botnet.shared.ascii.AsciiRow;
import cz.ophite.mimic.vhackos.botnet.shared.ascii.converter.AsciiBooleanConverter;

import java.util.List;

/**
 * Odpověď ze skenování IP.
 *
 * @author mimic
 */
public final class NetworkScanResponse extends Response {

    @AsciiRow(value = "Tutorial", converter = AsciiBooleanConverter.class)
    @ResponseKey("tutor")
    private Integer tutorial;
    public static final String P_TUTORIAL = "tutorial";

    @AsciiRow("Exploits")
    @ResponseKey("exploits")
    private Integer exploits;
    public static final String P_EXPLOITS = "exploits";

    @AsciiRow("Connections")
    @ResponseKey("connectionCount")
    private Integer connectionCount;
    public static final String P_CONNECTION_COUNT = "connectionCount";

    @AsciiRow("IPs")
    @ResponseKey(value = KEY_IPS)
    private List<IpScanData> ips;
    public static final String P_IPS = "ips";
    public static final String KEY_IPS = "ips";

    @AsciiRow("Bruted IPs")
    @ResponseKey(value = "cm")
    private List<IpBruteData> brutedIps;
    public static final String P_BRUTED_IPS = "brutedIps";

    public Integer getTutorial() {
        return tutorial;
    }

    public void setTutorial(Integer tutorial) {
        this.tutorial = tutorial;
    }

    public Integer getExploits() {
        return exploits;
    }

    public void setExploits(Integer exploits) {
        this.exploits = exploits;
    }

    public Integer getConnectionCount() {
        return connectionCount;
    }

    public void setConnectionCount(Integer connectionCount) {
        this.connectionCount = connectionCount;
    }

    public List<IpScanData> getIps() {
        return ips;
    }

    public void setIps(List<IpScanData> ips) {
        this.ips = ips;
    }

    public List<IpBruteData> getBrutedIps() {
        return brutedIps;
    }

    public void setBrutedIps(List<IpBruteData> brutedIps) {
        this.brutedIps = brutedIps;
    }
}
