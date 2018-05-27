package cz.ophite.mimic.vhackos.botnet.api.module;

import cz.ophite.mimic.vhackos.botnet.api.IBotnet;
import cz.ophite.mimic.vhackos.botnet.api.exception.BotnetException;
import cz.ophite.mimic.vhackos.botnet.api.exception.RegisterException;
import cz.ophite.mimic.vhackos.botnet.api.module.base.Module;
import cz.ophite.mimic.vhackos.botnet.api.module.base.ModuleHelper;
import cz.ophite.mimic.vhackos.botnet.api.net.response.LeaderboardsResponse;
import cz.ophite.mimic.vhackos.botnet.api.net.response.LoginResponse;
import cz.ophite.mimic.vhackos.botnet.api.net.response.UpdateResponse;
import cz.ophite.mimic.vhackos.botnet.api.opcode.LeaderboardsOpcode;
import cz.ophite.mimic.vhackos.botnet.api.opcode.LoginOpcode;
import cz.ophite.mimic.vhackos.botnet.api.opcode.RegisterOpcode;
import cz.ophite.mimic.vhackos.botnet.api.opcode.UpdateOpcode;
import cz.ophite.mimic.vhackos.botnet.shared.injection.Inject;
import cz.ophite.mimic.vhackos.botnet.shared.utils.HashUtils;

import java.util.Collections;
import java.util.Map;

/**
 * Modul pro přihlášení uživatele.
 *
 * @author mimic
 */
@Inject
public final class CommonModule extends Module {

    private static final String ERR_CODE_USER_ALREADY_EXISTS = "1";
    private static final String ERR_CODE_EMAIL_IS_ALREADY_USED = "3";

    protected CommonModule(IBotnet botnet) {
        super(botnet);
    }

    /**
     * Přihlásí uživatele.
     */
    public synchronized LoginResponse login() {
        var opcode = new LoginOpcode();
        opcode.setUserName(getConfig().getUserName());
        opcode.setPasswordHash(HashUtils.toHexMd5(getConfig().getPassword()));

        var response = sendRequest(opcode);
        var dto = createLoginResponse(response);

        var connData = getBotnet().getConnectionData();
        connData.setUserName(dto.getUserName());
        connData.setAccessToken(dto.getAccessToken());
        connData.setUid(dto.getUid());

        return dto;
    }

    /**
     * Zaregistruje nového uživatele.
     */
    public synchronized LoginResponse register(String userName, String password, String email) {
        var opcode = new RegisterOpcode();
        opcode.setUserName(userName);
        opcode.setPasswordHash(HashUtils.toHexMd5(password));
        opcode.setEmail(email);
        opcode.setLanguage(getBotnet().getConnectionData().getLang());

        try {
            var response = sendRequest(opcode);
            return createLoginResponse(response);

        } catch (BotnetException e) {
            if (ERR_CODE_USER_ALREADY_EXISTS.equals(e.getResultCode())) {
                throw new RegisterException(e.getResultCode(), "The username '" + userName + "' is already used");
            } else if (ERR_CODE_EMAIL_IS_ALREADY_USED.equals(e.getResultCode())) {
                throw new RegisterException(e.getResultCode(), "This email '" + email + "' is already in use");
            }
            throw e;
        }
    }

    /**
     * Získá aktuální informace o uživateli.
     */
    public synchronized UpdateResponse update() {
        var opcode = new UpdateOpcode();
        var response = sendRequest(opcode);
        var dto = new UpdateResponse();

        ModuleHelper.checkResponseIntegrity(response.keySet(), UpdateResponse.class);
        ModuleHelper.setField(response, dto, UpdateResponse.P_ACCESS_TOKEN);
        ModuleHelper.setField(response, dto, UpdateResponse.P_UID);
        ModuleHelper.setField(response, dto, UpdateResponse.P_USERNAME);
        ModuleHelper.setField(response, dto, UpdateResponse.P_EXPIRED);
        ModuleHelper.setField(response, dto, UpdateResponse.P_EASTER_EVENT);
        ModuleHelper.setField(response, dto, UpdateResponse.P_BLUE);
        ModuleHelper.setField(response, dto, UpdateResponse.P_GREEN);
        ModuleHelper.setField(response, dto, UpdateResponse.P_GREY);
        ModuleHelper.setField(response, dto, UpdateResponse.P_YELLOW);
        ModuleHelper.setField(response, dto, UpdateResponse.P_ORANGE);
        ModuleHelper.setField(response, dto, UpdateResponse.P_PURPLE);
        ModuleHelper.setField(response, dto, UpdateResponse.P_RED);
        ModuleHelper.setField(response, dto, UpdateResponse.P_TURKIS);
        ModuleHelper.setField(response, dto, UpdateResponse.P_WHITE);
        ModuleHelper.setField(response, dto, UpdateResponse.P_EGGS);
        ModuleHelper.setField(response, dto, UpdateResponse.P_NEW_MESSAGE);
        ModuleHelper.setField(response, dto, UpdateResponse.P_UNREAD_COUNT);
        ModuleHelper.setField(response, dto, UpdateResponse.P_EXPLOITS);
        ModuleHelper.setField(response, dto, UpdateResponse.P_EXPERIENCE);
        ModuleHelper.setField(response, dto, UpdateResponse.P_REQUIRED_EXPERIENCE);
        ModuleHelper.setField(response, dto, UpdateResponse.P_EXP_PC);
        ModuleHelper.setField(response, dto, UpdateResponse.P_NETCOINS);
        ModuleHelper.setField(response, dto, UpdateResponse.P_LEVEL);
        ModuleHelper.setField(response, dto, UpdateResponse.P_MONEY);
        ModuleHelper.setField(response, dto, UpdateResponse.P_IP);
        ModuleHelper.setField(response, dto, UpdateResponse.P_APP_FIREWALL);
        ModuleHelper.setField(response, dto, UpdateResponse.P_APP_ANTIVIRUS);
        ModuleHelper.setField(response, dto, UpdateResponse.P_APP_SDK);
        ModuleHelper.setField(response, dto, UpdateResponse.P_C_COLOR);
        ModuleHelper.setField(response, dto, UpdateResponse.P_APP_BRUTEFORCE);
        ModuleHelper.setField(response, dto, UpdateResponse.P_APP_SPAM);
        ModuleHelper.setField(response, dto, UpdateResponse.P_MALWARE_KIT);
        ModuleHelper.setField(response, dto, UpdateResponse.P_MODERATOR);
        ModuleHelper.setField(response, dto, UpdateResponse.P_CREW);
        ModuleHelper.setField(response, dto, UpdateResponse.P_MINER);
        ModuleHelper.setField(response, dto, UpdateResponse.P_TIME);
        ModuleHelper.setField(response, dto, UpdateResponse.P_SERVER);
        ModuleHelper.setField(response, dto, UpdateResponse.P_MINER_LEFT);
        ModuleHelper.setField(response, dto, UpdateResponse.P_CHAT_BAN);
        ModuleHelper.setField(response, dto, UpdateResponse.P_COM_COUNT);
        ModuleHelper.setField(response, dto, UpdateResponse.P_VIP);
        ModuleHelper.setField(response, dto, UpdateResponse.P_CREW_MSG_COUNT);
        ModuleHelper.setField(response, dto, UpdateResponse.P_NOTEPAD);
        ModuleHelper.setField(response, dto, UpdateResponse.P_LEADERBOARD);
        ModuleHelper.setField(response, dto, UpdateResponse.P_MISSIONS);
        ModuleHelper.setField(response, dto, UpdateResponse.P_JOBS);
        ModuleHelper.setField(response, dto, UpdateResponse.P_COMMUNITY);
        ModuleHelper.setField(response, dto, UpdateResponse.P_RUNNING_TASKS);
        ModuleHelper.setField(response, dto, UpdateResponse.P_INTERNET_CONNECTION);

        return dto;
    }

    /**
     * Získá informace z leaderboards.
     */
    public synchronized LeaderboardsResponse getLeaderboards() {
        var opcode = new LeaderboardsOpcode();
        var response = sendRequest(opcode);

        var dto = new LeaderboardsResponse();
        ModuleHelper.checkResponseIntegrity(response.keySet(), LeaderboardsResponse.class);
        ModuleHelper.setField(response, dto, LeaderboardsResponse.P_COUNT);
        ModuleHelper.setField(response, dto, LeaderboardsResponse.P_MY_LEVEL);
        ModuleHelper.setField(response, dto, LeaderboardsResponse.P_MY_EXP);
        ModuleHelper.setField(response, dto, LeaderboardsResponse.P_MY_EXP_REQ);
        ModuleHelper.setField(response, dto, LeaderboardsResponse.P_EXP_GAIN);
        ModuleHelper.setField(response, dto, LeaderboardsResponse.P_TOURNAMENT_LEFT);
        ModuleHelper.setField(response, dto, LeaderboardsResponse.P_TOURNAMENT_RANK);
        ModuleHelper.setField(response, dto, LeaderboardsResponse.P_MY_RANK);
        ModuleHelper.setField(response, dto, LeaderboardsResponse.P_CREW_COUNT);

        if (!ModuleHelper.setField(response, dto, LeaderboardsResponse.P_LEADERBOARD_DATA, (f, data) -> ModuleHelper
                .convertToLeaderboardData(data))) {
            dto.setLeaderboardData(Collections.emptyList());
        }
        if (!ModuleHelper.setField(response, dto, LeaderboardsResponse.P_TOURNAMENT_DATA, (f, data) -> ModuleHelper
                .convertToTournament24HData(data))) {
            dto.setTournamentData(Collections.emptyList());
        }
        if (!ModuleHelper.setField(response, dto, LeaderboardsResponse.P_CREWS_DATA, (f, data) -> ModuleHelper
                .convertToLeaderboardCrewData(data))) {
            dto.setCrewsData(Collections.emptyList());
        }
        return dto;
    }

    private LoginResponse createLoginResponse(Map<String, Object> response) {
        var dto = new LoginResponse();
        ModuleHelper.checkResponseIntegrity(response.keySet(), LoginResponse.class);
        ModuleHelper.setField(response, dto, LoginResponse.P_USER_NAME);
        ModuleHelper.setField(response, dto, LoginResponse.P_ACCESS_TOKEN);
        ModuleHelper.setField(response, dto, LoginResponse.P_UID);
        return dto;
    }
}
