package cz.ophite.mimic.vhackos.botnet.api.module;

import cz.ophite.mimic.vhackos.botnet.api.IBotnet;
import cz.ophite.mimic.vhackos.botnet.api.exception.BotnetException;
import cz.ophite.mimic.vhackos.botnet.api.exception.RemoteException;
import cz.ophite.mimic.vhackos.botnet.api.module.base.Module;
import cz.ophite.mimic.vhackos.botnet.api.opcode.LogOpcode;
import cz.ophite.mimic.vhackos.botnet.api.opcode.RemoteLogOpcode;
import cz.ophite.mimic.vhackos.botnet.api.opcode.UpdateLogOpcode;
import cz.ophite.mimic.vhackos.botnet.api.opcode.UpdateRemoteLogOpcode;
import cz.ophite.mimic.vhackos.botnet.shared.injection.Inject;

import java.util.ArrayList;
import java.util.List;

/**
 * Modul pro logování.
 *
 * @author mimic
 */
@Inject
public class LogModule extends Module {

    private static final String OK_CODE_LOG_SET = "2";
    private static final String ERR_CODE_IP_NOT_AVAILABLE = "22";

    protected LogModule(IBotnet botnet) {
        super(botnet);
    }

    /**
     * Získá vlastní log.
     */
    public synchronized List<String> getLog() {
        var opcode = new LogOpcode();
        var response = sendRequest(opcode);
        var data = response.get("logs").toString();
        return toLogList(data);
    }

    /**
     * Nastaví vlastní log.
     */
    public synchronized void setLog(String log) {
        var opcode = new UpdateLogOpcode();
        opcode.setLog(log);

        try {
            sendRequest(opcode);

        } catch (BotnetException e) {
            if (!OK_CODE_LOG_SET.equals(e.getResultCode())) {
                throw e;
            }
        }
    }

    /**
     * Získá obsah vzdáleného logu IP. Vrací řádky v logu.
     */
    public synchronized List<String> getRemoteLog(String ip) {
        var opcode = new RemoteLogOpcode();
        opcode.setTargetIp(ip);

        var response = sendRequest(opcode);
        var result = getResultValue(response);

        if (result == null || result.equals(ERR_CODE_IP_NOT_AVAILABLE)) {
            throw new RemoteException(result, "Target IP '" + ip + "' is not available or broken");
        }
        var data = response.get("logs").toString();
        return toLogList(data);
    }

    /**
     * Nastaví obsah vzdáleného logu IP.
     */
    public synchronized void setRemoteLog(String ip, String log) {
        var opcode = new UpdateRemoteLogOpcode();
        opcode.setTargetIp(ip);
        opcode.setLog(log);

        try {
            var response = sendRequest(opcode);
            var result = getResultValue(response);

            if (result == null || result.equals(ERR_CODE_IP_NOT_AVAILABLE)) {
                throw new RemoteException(result, "Target IP '" + ip + "' is not available or broken");
            }
        } catch (BotnetException e) {
            if (!OK_CODE_LOG_SET.equals(e.getResultCode())) {
                throw e;
            }
        }
    }

    private static List<String> toLogList(String logs) {
        var data = logs.split("\n");
        var list = new ArrayList<String>(data.length);

        for (var line : data) {
            // nastaví mezery na nezalomitelné znaky, protože v logu můžou být i ASCII obrazce
            list.add(line.replace((char) 32, (char) 160));
        }
        return list;
    }
}
