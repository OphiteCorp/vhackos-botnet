package cz.ophite.mimic.vhackos.botnet.api.module;

import cz.ophite.mimic.vhackos.botnet.api.IBotnet;
import cz.ophite.mimic.vhackos.botnet.api.exception.BotnetException;
import cz.ophite.mimic.vhackos.botnet.api.exception.ExploitException;
import cz.ophite.mimic.vhackos.botnet.api.exception.InvalidAccessTokenException;
import cz.ophite.mimic.vhackos.botnet.api.exception.IpNotExistsException;
import cz.ophite.mimic.vhackos.botnet.api.module.base.Module;
import cz.ophite.mimic.vhackos.botnet.api.module.base.ModuleHelper;
import cz.ophite.mimic.vhackos.botnet.api.net.response.ExploitResponse;
import cz.ophite.mimic.vhackos.botnet.api.net.response.NetworkScanResponse;
import cz.ophite.mimic.vhackos.botnet.api.opcode.ExploitSystemOpcode;
import cz.ophite.mimic.vhackos.botnet.api.opcode.NetworkScanOpcode;
import cz.ophite.mimic.vhackos.botnet.shared.injection.Inject;
import org.apache.commons.lang3.StringUtils;

import java.util.Collections;

/**
 * Modul pro práci se sítí.
 *
 * @author mimic
 */
@Inject
public final class NetworkModule extends Module {

    private static final String ERR_CODE_IP_NOT_EXISTS = "1";
    private static final String ERR_CODE_TOO_HIGH_FIREWALL = "3";
    private static final String ERR_CODE_IP_ALREADY_EXPLOITED = "5";

    protected NetworkModule(IBotnet botnet) {
        super(botnet);
    }

    /**
     * Vyhledá další sadu IP pro exploit.
     */
    public synchronized NetworkScanResponse scan() {
        var opcode = new NetworkScanOpcode();
        var response = sendRequest(opcode);
        var result = getResultValue(response);

        // nestačí pouze kontrola resultu na null, protože pří správné odpovědi též vrací null, ale IPs musí
        // obsahovat vždy
        if (StringUtils.isEmpty(result) && response.getOrDefault(NetworkScanResponse.KEY_IPS, null) == null) {
            throw new InvalidAccessTokenException(null, "Your access token is no longer valid, try signing in again");
        }
        var dto = new NetworkScanResponse();
        ModuleHelper.checkResponseIntegrity(response.keySet(), NetworkScanResponse.class);
        ModuleHelper.setField(response, dto, NetworkScanResponse.P_TUTORIAL);
        ModuleHelper.setField(response, dto, NetworkScanResponse.P_EXPLOITS);
        ModuleHelper.setField(response, dto, NetworkScanResponse.P_CONNECTION_COUNT);
        ModuleHelper.setField(response, dto, NetworkScanResponse.P_IPS, (f, data) -> ModuleHelper
                .convertToIpScanData(data));

        if (!ModuleHelper.setField(response, dto, NetworkScanResponse.P_BRUTED_IPS, (f, data) -> ModuleHelper
                .convertToIpBruteData(data))) {
            dto.setBrutedIps(Collections.emptyList());
        }
        return dto;
    }

    /**
     * Exploituje cílovou IP.
     */
    public synchronized ExploitResponse exploit(String ip) {
        var opcode = new ExploitSystemOpcode();
        opcode.setTargetIp(ip);

        try {
            var response = sendRequest(opcode);
            var result = getResultValue(response);

            // IP byla nebo je již exploitována
            if (result.equals(ERR_CODE_IP_ALREADY_EXPLOITED)) {
                throw new ExploitException(result, "The IP address '" + ip + "' is already being exploited");

            } else if (result.equals(ERR_CODE_TOO_HIGH_FIREWALL)) {
                throw new ExploitException(result, "The IP address '" + ip + "' has a higher firewall than your SDK");
            }
            var dto = new ExploitResponse();
            ModuleHelper.checkResponseIntegrity(response.keySet(), ExploitResponse.class);
            ModuleHelper.setField(response, dto, ExploitResponse.P_EXPLOITS);
            ModuleHelper.setField(response, dto, ExploitResponse.P_CONNECTION_COUNT);

            if (!ModuleHelper.setField(response, dto, ExploitResponse.P_BRUTED_IPS, (f, data) -> ModuleHelper
                    .convertToIpBruteData(data))) {
                dto.setBrutedIps(Collections.emptyList());
            }
            return dto;

        } catch (BotnetException e) {
            if (ERR_CODE_IP_NOT_EXISTS.equals(e.getResultCode())) {
                throw new IpNotExistsException(e.getResultCode(), "The IP address '" + ip + "' no longer exists");
            }
            throw e;
        }
    }
}
