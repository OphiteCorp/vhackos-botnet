package cz.ophite.mimic.vhackos.botnet.api.module;

import cz.ophite.mimic.vhackos.botnet.api.IBotnet;
import cz.ophite.mimic.vhackos.botnet.api.exception.BotnetException;
import cz.ophite.mimic.vhackos.botnet.api.exception.RemoteException;
import cz.ophite.mimic.vhackos.botnet.api.module.base.Module;
import cz.ophite.mimic.vhackos.botnet.api.module.base.ModuleHelper;
import cz.ophite.mimic.vhackos.botnet.api.net.response.RemoteSystemResponse;
import cz.ophite.mimic.vhackos.botnet.api.opcode.RemoteTargetOpcode;
import cz.ophite.mimic.vhackos.botnet.shared.injection.Inject;

/**
 * Modul obsahující metody pro vzdálenou kontrolu uživatele. Aby metody bylo možné zavolat, tak cíl musí být exploitnut.
 *
 * @author mimic
 */
@Inject
public final class RemoteModule extends Module {

    private static final String ERR_CODE_IP_NOT_AVAILABLE_FOR_REMOTE = "1";

    protected RemoteModule(IBotnet botnet) {
        super(botnet);
    }

    /**
     * Získá informace o vzdáleném cíli.
     */
    public synchronized RemoteSystemResponse getSystemInfo(String ip) {
        var opcode = new RemoteTargetOpcode();
        opcode.setTargetIp(ip);

        try {
            var response = sendRequest(opcode);
            var result = getResultValue(response);

            if (result == null) {
                throw new RemoteException(result, "IP '" + ip + "' does not exist or you do not have permissions");
            }
            var dto = new RemoteSystemResponse();
            ModuleHelper.checkResponseIntegrity(response.keySet(), RemoteSystemResponse.class);
            ModuleHelper.setField(response, dto, RemoteSystemResponse.P_CUSTOM_BACKGROUND);
            ModuleHelper.setField(response, dto, RemoteSystemResponse.P_IP);
            ModuleHelper.setField(response, dto, RemoteSystemResponse.P_REMOTE_IP);
            ModuleHelper.setField(response, dto, RemoteSystemResponse.P_LEVEL);
            ModuleHelper.setField(response, dto, RemoteSystemResponse.P_USER_NAME);
            ModuleHelper.setField(response, dto, RemoteSystemResponse.P_NOTEPAD);
            ModuleHelper.setField(response, dto, RemoteSystemResponse.P_LEADERBOARD);
            ModuleHelper.setField(response, dto, RemoteSystemResponse.P_MISSIONS);
            ModuleHelper.setField(response, dto, RemoteSystemResponse.P_JOBS);
            ModuleHelper.setField(response, dto, RemoteSystemResponse.P_COMMUNITY);
            ModuleHelper.setField(response, dto, RemoteSystemResponse.P_INTERNET_CONNECTION);

            return dto;

        } catch (BotnetException e) {
            if (ERR_CODE_IP_NOT_AVAILABLE_FOR_REMOTE.equals(e.getResultCode())) {
                throw new RemoteException(e
                        .getResultCode(), "IP '" + ip + "' does not exist or you do not have permissions");
            }
            throw e;
        }
    }
}
