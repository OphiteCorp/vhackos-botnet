package cz.ophite.mimic.vhackos.botnet.api.net.response;

import cz.ophite.mimic.vhackos.botnet.api.net.response.base.Response;
import cz.ophite.mimic.vhackos.botnet.api.net.response.base.ResponseKey;
import cz.ophite.mimic.vhackos.botnet.shared.utils.ascii.AsciiRow;
import cz.ophite.mimic.vhackos.botnet.shared.utils.ascii.converter.AsciiBooleanConverter;
import cz.ophite.mimic.vhackos.botnet.shared.utils.ascii.converter.AsciiElapsedSecondsTimeConverter;

/**
 * Odpověď serveru na získání SDK.
 *
 * @author mimic
 */
public final class SdkResponse extends Response {

    @AsciiRow(value = "Applied", converter = AsciiBooleanConverter.class)
    @ResponseKey("applied")
    private Integer applied;
    public static final String P_APPLIED = "applied";

    @AsciiRow("SDK")
    @ResponseKey("sdk")
    private Integer sdk;
    public static final String P_SDK = "sdk";

    @AsciiRow("Exploits")
    @ResponseKey("exploits")
    private Integer exploits;
    public static final String P_EXPLOITS = "exploits";

    @AsciiRow(value = "Next Exploit", converter = AsciiElapsedSecondsTimeConverter.class)
    @ResponseKey("nextexploit")
    private Long nextExploit;
    public static final String P_NEXT_EXPLOIT = "nextExploit";

    public Integer getApplied() {
        return applied;
    }

    public void setApplied(Integer applied) {
        this.applied = applied;
    }

    public Integer getSdk() {
        return sdk;
    }

    public void setSdk(Integer sdk) {
        this.sdk = sdk;
    }

    public Integer getExploits() {
        return exploits;
    }

    public void setExploits(Integer exploits) {
        this.exploits = exploits;
    }

    public Long getNextExploit() {
        return nextExploit;
    }

    public void setNextExploit(Long nextExploit) {
        this.nextExploit = nextExploit;
    }
}
