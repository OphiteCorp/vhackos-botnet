package cz.ophite.mimic.vhackos.botnet.command;

import cz.ophite.mimic.vhackos.botnet.Application;
import cz.ophite.mimic.vhackos.botnet.api.exception.IpNotExistsException;
import cz.ophite.mimic.vhackos.botnet.api.module.*;
import cz.ophite.mimic.vhackos.botnet.api.net.response.*;
import cz.ophite.mimic.vhackos.botnet.api.net.response.data.*;
import cz.ophite.mimic.vhackos.botnet.command.base.BaseCommand;
import cz.ophite.mimic.vhackos.botnet.config.ApplicationConfig;
import cz.ophite.mimic.vhackos.botnet.config.ConfigProvider;
import cz.ophite.mimic.vhackos.botnet.db.service.DatabaseService;
import cz.ophite.mimic.vhackos.botnet.shared.command.Command;
import cz.ophite.mimic.vhackos.botnet.shared.command.CommandParam;
import cz.ophite.mimic.vhackos.botnet.shared.injection.Autowired;
import cz.ophite.mimic.vhackos.botnet.shared.injection.Inject;
import cz.ophite.mimic.vhackos.botnet.shared.injection.InjectionContext;
import cz.ophite.mimic.vhackos.botnet.shared.utils.ascii.AsciiMaker;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Definuje dostupné příkazy pro botnet.
 *
 * @author mimic
 */
@Inject
final class BotnetCommands extends BaseCommand {

    @Autowired
    private ConfigProvider configProvider;

    @Autowired
    private ApplicationConfig config;

    @Autowired
    private DatabaseService databaseService;

    @Autowired
    private CommonModule commonModule;

    @Autowired
    private NetworkModule networkModule;

    @Autowired
    private RemoteModule remoteModule;

    @Autowired
    private MalwareModule malwareModule;

    @Autowired
    private NotepadModule notepadModule;

    @Autowired
    private ProfileModule profileModule;

    @Autowired
    private BuyModule buyModule;

    @Autowired
    private SdkModule sdkModule;

    /**
     * Vypíše logo.
     */
    @Command(value = "logo", comment = "Prints the logo")
    private String logo() {
        return execute("logo", am -> {
            am.setTopTheme();
            put(am, " ", Application.LOGO);
        });
    }

    /**
     * Přenačte konfiguraci aplikace.
     */
    @Command(value = "reload", comment = "Forces reloading configuration")
    private String reloadConfig() {
        return execute("reload configuration", am -> {
            var config = configProvider.getAppConfig();
            InjectionContext.getInstance().get(ApplicationConfig.class).set(config);
            put(am, "Info", "The configuration has been reloaded");
        });
    }

    /**
     * Získá konfiguraci.
     */
    @Command(value = "config", comment = "Get the current configuration")
    private String getConfig() {
        return execute("configuration", am -> {
            var map = config.asMap();

            for (var entry : map.entrySet()) {
                put(am, entry.getKey(), entry.getValue());
            }
        });
    }

    /**
     * Přihlásí uživatele znovu.
     */
    @Command(value = "login", comment = "Signs in again and gets a new token")
    private String login() {
        return execute("login", am -> {
            var data = commonModule.login();
            var fields = getFields(data, true);
            putRemainings(am, fields, false);
        });
    }

    /**
     * Zaregistruje nového uživatele.
     */
    @Command(value = "register", comment = "Registering a new user")
    private String register(@CommandParam("user") String userName, @CommandParam("password") String password,
            @CommandParam("email") String email) {

        return execute("register", am -> {
            var data = commonModule.register(userName, password, email);
            var fields = getFields(data, true);
            putRemainings(am, fields, false);
        });
    }

    /**
     * Získá aktuální informace o uživateli.
     */
    @Command(value = "update", comment = "Gets up-to-date information about the user")
    private String update() {
        return execute("update", am -> {
            var data = commonModule.update();
            var fields = getFields(data, true);

            put(am, fields.remove(UpdateResponse.P_ACCESS_TOKEN));
            put(am, fields.remove(UpdateResponse.P_UID));
            put(am, fields.remove(UpdateResponse.P_USERNAME));
            put(am, fields.remove(UpdateResponse.P_EMAIL));
            put(am, fields.remove(UpdateResponse.P_APP_ANTIVIRUS));
            put(am, fields.remove(UpdateResponse.P_APP_BRUTEFORCE));
            put(am, fields.remove(UpdateResponse.P_APP_FIREWALL));
            put(am, fields.remove(UpdateResponse.P_APP_SDK));
            put(am, fields.remove(UpdateResponse.P_APP_SPAM));
            put(am, fields.remove(UpdateResponse.P_CHAT_BAN));
            put(am, fields.remove(UpdateResponse.P_COMMUNITY));
            put(am, fields.remove(UpdateResponse.P_CREW));
            put(am, fields.remove(UpdateResponse.P_CREW_MSG_COUNT));
            put(am, fields.remove(UpdateResponse.P_EXPERIENCE));
            put(am, fields.remove(UpdateResponse.P_EXPIRED));
            put(am, fields.remove(UpdateResponse.P_EXPLOITS));
            put(am, fields.remove(UpdateResponse.P_INTERNET_CONNECTION));
            put(am, fields.remove(UpdateResponse.P_IP));
            put(am, fields.remove(UpdateResponse.P_JOBS));
            put(am, fields.remove(UpdateResponse.P_LEADERBOARD));
            put(am, fields.remove(UpdateResponse.P_LEVEL));
            put(am, fields.remove(UpdateResponse.P_MALWARE_KIT));
            put(am, fields.remove(UpdateResponse.P_MINER));
            put(am, fields.remove(UpdateResponse.P_MINER_LEFT));
            put(am, fields.remove(UpdateResponse.P_MISSIONS));
            put(am, fields.remove(UpdateResponse.P_MODERATOR));
            put(am, fields.remove(UpdateResponse.P_VIP));
            put(am, fields.remove(UpdateResponse.P_MONEY));
            put(am, fields.remove(UpdateResponse.P_NETCOINS));
            put(am, fields.remove(UpdateResponse.P_NEW_MESSAGE));
            put(am, fields.remove(UpdateResponse.P_NOTEPAD));
            put(am, fields.remove(UpdateResponse.P_REQUIRED_EXPERIENCE));
            put(am, fields.remove(UpdateResponse.P_SERVER));
            put(am, fields.remove(UpdateResponse.P_RUNNING_TASKS));
            putRemainings(am, fields);
        });
    }

    /**
     * Skenuje síť (získá vždy 10 IP adress, dle nastavení serveru).
     */
    @Command(value = "scan", comment = "It scans the network")
    private String scan() {
        return execute("scan", am -> {
            var data = networkModule.scan();
            var fields = getFields(data, true);

            convertScannedIps(am, fields.remove(NetworkScanResponse.P_IPS));
            put(am, fields.remove(NetworkScanResponse.P_EXPLOITS));
            put(am, fields.remove(NetworkScanResponse.P_CONNECTION_COUNT));
            convertBrutedIps(am, fields.remove(NetworkScanResponse.P_BRUTED_IPS));
            putRemainings(am, fields);
        });
    }

    /**
     * Exploituje IP.
     */
    @Command(value = "exploit", comment = "Exploits the IP address")
    private String exploit(@CommandParam("ip") String ip) {
        return execute("exploit -> " + ip, am -> {
            try {
                var data = networkModule.exploit(ip);
                var fields = getFields(data, true);

                convertBrutedIps(am, fields.remove(ExploitResponse.P_BRUTED_IPS));
                put(am, fields.remove(ExploitResponse.P_EXPLOITS));
                put(am, fields.remove(ExploitResponse.P_CONNECTION_COUNT));
                putRemainings(am, fields);

            } catch (IpNotExistsException e) {
                databaseService.invalidIp(ip);
                throw e;
            }
        });
    }

    /**
     * Získá informace o SDK.
     */
    @Command(value = "sdk", comment = "Gets information about the SDK")
    private String getSdk() {
        return execute("sdk", am -> {
            var data = sdkModule.getSdk();
            var fields = getFields(data, true);
            addSdkResponseToAsciiMaker(am, fields);
        });
    }

    /**
     * Koupí další SDK pro exploit.
     */
    @Command(value = "sdk buy", comment = "Buys the SDK for netcoins")
    private String buySdk() {
        return execute("sdk buy", am -> {
            var data = sdkModule.buySdk();
            var fields = getFields(data, true);
            addSdkResponseToAsciiMaker(am, fields);
        });
    }

    /**
     * Získá informace o malwaru.
     */
    @Command(value = "malware", comment = "Gets information about malware")
    private String getMalware() {
        return execute("malware", am -> {
            var data = malwareModule.getMalwareKit();
            var fields = getFields(data, true);
            addMalwareResponseToAsciiMaker(am, fields);
        });
    }

    /**
     * Vytvoří nový malware.
     */
    @Command(value = "malware create", comment = "Creates new malware")
    private String createMalware() {
        return execute("malware create", am -> {
            var data = malwareModule.createMalwareKit();
            var fields = getFields(data, true);
            addMalwareResponseToAsciiMaker(am, fields);
        });
    }

    /**
     * Získá vlastní poznámkový blok.
     */
    @Command(value = "notepad", comment = "Gets own notepad")
    private String getNotepad() {
        return execute("notepad", am -> {
            var data = notepadModule.getNotepad();

            for (var i = 0; i < data.size(); i++) {
                put(am, (i == 0) ? "Notepad" : "", data.get(i));
            }
        });
    }

    /**
     * Získá vlastní poznámkový blok.
     */
    @Command(value = "notepad set", comment = "Sets own notepad")
    private String setNotepad(@CommandParam("lines") String lines) {
        return execute("notepad set", am -> {
            var list = Arrays.asList(lines.split("\\\\n"));
            notepadModule.setNotepad(list);
            put(am, "Result", "Notepad has been modified");
        });
    }

    /**
     * Získá informace o hacknutém systému.
     */
    @Command(value = "remote", comment = "Gets information about the remote system")
    private String getRemote(@CommandParam("ip") String ip) {
        return execute("remote -> " + ip, am -> {
            var data = remoteModule.getSystemInfo(ip);
            var fields = getFields(data, true);

            putRemainings(am, fields, false);
            databaseService.updateScanIp(data.getIp(), data.getUserName(), data.getLevel());
        });
    }

    /**
     * Získá profil uživatele.
     */
    @Command(value = "profile", comment = "Gets a user profile")
    private String getProfile(@CommandParam("uid") int userId) {
        return execute("user profile -> " + userId, am -> {
            am.setInsideTheme();

            var data = profileModule.getProfile(userId);
            var fields = getFields(data, true);
            addUserProfileResponseToAsciiMaker(am, fields);
        });
    }

    /**
     * Odešle žádost o prátelství.
     */
    @Command(value = "friend add", comment = "Sends a request for friendship")
    private String addFriend(@CommandParam("uid") int userId) {
        return execute("add friend -> " + userId, am -> {
            var data = profileModule.addFriend(userId);
            var fields = getFields(data, true);
            addUserProfileResponseToAsciiMaker(am, fields);
        });
    }

    /**
     * Zobrazí obchod, kde se kupují věci za peníze nebo netcoins.
     */
    @Command(value = "buy list", comment = "List of available items to buy")
    private String buyList() {
        return execute("buy list", am -> {
            var data = buyModule.getBuyList();
            var fields = getFields(data, true);
            addBuyResponseToAsciiMaker(am, fields);
        });
    }

    /**
     * Zobrazí obchod, kde se kupují věci za peníze nebo netcoins.
     */
    @Command(value = "change ip", comment = "Changes the IP address for 1,000 netcoins")
    private String changeIp() {
        return execute("change IP", am -> {
            var data = buyModule.buyNewIpAddress();
            var fields = getFields(data, true);
            addBuyResponseToAsciiMaker(am, fields);
        });
    }

    /**
     * Zobrazí leaderboards.
     */
    @Command(value = "leader", comment = "Displays leaderboards")
    private String getLeaderboards() {
        return execute("leaderboards", am -> {
            var data = commonModule.getLeaderboards();
            var fields = getFields(data, true);
            addLeaderboardsResponseToAsciiMaker(am, fields);
        });
    }

    // === Pomocné metody
    // ================================================================================================================

    private void addSdkResponseToAsciiMaker(AsciiMaker am, Map<String, BotnetCommands.FieldData> fields) {
        put(am, fields.remove(SdkResponse.P_SDK));
        put(am, fields.remove(SdkResponse.P_EXPLOITS));
        put(am, fields.remove(SdkResponse.P_NEXT_EXPLOIT));
        putRemainings(am, fields);
    }

    private void addUserProfileResponseToAsciiMaker(AsciiMaker am, Map<String, BotnetCommands.FieldData> fields) {
        var logo = fields.remove(UserProfileResponse.P_CREW_LOGO);
        put(am, logo);
        putRemainings(am, fields, logo != null);
    }

    private void addBuyResponseToAsciiMaker(AsciiMaker am, Map<String, BotnetCommands.FieldData> fields) {
        put(am, fields.remove(BuyResponse.P_NEXT_INET));
        put(am, fields.remove(BuyResponse.P_REQ_LEVEL_INET));
        put(am, fields.remove(BuyResponse.P_IP_CHANGE));
        convertToBuyItems(am, fields.remove(BuyResponse.P_ITEMS));
        am.addRule();
        convertToSKUs(am, fields.remove(BuyResponse.P_SKUS));
        putRemainings(am, fields);
    }

    private void addMalwareResponseToAsciiMaker(AsciiMaker am, Map<String, BotnetCommands.FieldData> fields) {
        put(am, fields.remove(MalwareKitResponse.P_MALWARE_KIT));
        put(am, fields.remove(MalwareKitResponse.P_MALWARES));
        put(am, fields.remove(MalwareKitResponse.P_BANKLOG_LEFT));
        put(am, fields.remove(MalwareKitResponse.P_NEXT_EXPLOIT));
        putRemainings(am, fields);
    }

    private void addLeaderboardsResponseToAsciiMaker(AsciiMaker am, Map<String, BotnetCommands.FieldData> fields) {
        var lbData = fields.remove(LeaderboardsResponse.P_LEADERBOARD_DATA);
        var tournamentData = fields.remove(LeaderboardsResponse.P_TOURNAMENT_DATA);
        var crewData = fields.remove(LeaderboardsResponse.P_CREWS_DATA);
        var left = fields.remove(LeaderboardsResponse.P_TOURNAMENT_LEFT);

        put(am, fields.remove(LeaderboardsResponse.P_MY_RANK));
        put(am, fields.remove(LeaderboardsResponse.P_TOURNAMENT_RANK));
        put(am, left);

        long exp = (long) fields.remove(LeaderboardsResponse.P_MY_EXP).value;
        long reqExp = (long) fields.remove(LeaderboardsResponse.P_MY_EXP_REQ).value;
        var percent = String.format("%.3f%%", (100. / reqExp) * exp);
        var expStr = String.format("%s / %s | %s", StringUtils.leftPad(String.valueOf(exp), 9), StringUtils
                .rightPad(String.valueOf(reqExp), 9), StringUtils.rightPad(percent, 6));
        put(am, "Experience", expStr);

        putRemainings(am, fields);

        am.addRule();
        convertLeaderboardData(am, lbData);
        am.addRule();
        convertTournament24HData(am, tournamentData, (long) left.rawValue);
        am.addRule();
        convertLeaderboardCrewData(am, crewData);
    }

    private void convertToSKUs(AsciiMaker am, FieldData data) {
        var SKUs = (List<String>) data.value;
        var name = data.name;

        for (var i = 0; i < SKUs.size(); i++) {
            put(am, (i == 0) ? name : "", SKUs.get(i));
        }
    }

    private void convertToBuyItems(AsciiMaker am, FieldData data) {
        var items = (List<BuyItemData>) data.value;
        var name = data.name;

        for (var i = 0; i < items.size(); i++) {
            var item = items.get(i);
            var str = String.format("%s | Price: %s ", StringUtils.rightPad(item.getSku(), 18), StringUtils
                    .leftPad(item.getPrice().toString(), 4));

            put(am, (i == 0) ? name : "", str);
        }
    }

    private void convertScannedIps(AsciiMaker am, FieldData data) {
        var ips = (List<IpScanData>) data.value;
        var name = data.name;

        for (var i = 0; i < ips.size(); i++) {
            var ip = ips.get(i);
            var str = String.format("%s | Level = %s | FW = %s", StringUtils.rightPad(ip.getIp(), 15), StringUtils
                    .leftPad(ip.getLevel().toString(), 3), StringUtils.leftPad(ip.getFirewall().toString(), 5));

            put(am, (i == 0) ? name : "", str);
            databaseService.addScanIp(ip);
        }
    }

    private void convertBrutedIps(AsciiMaker am, FieldData data) {
        var ips = (List<IpBruteData>) data.value;
        var name = data.name;

        for (var i = 0; i < ips.size(); i++) {
            var ip = ips.get(i);
            var fields = getFields(ip, true);
            var fIp = fields.get(IpBruteData.P_IP).value;
            var fUser = fields.get(IpBruteData.P_USER_NAME).value;
            var fBrute = fields.get(IpBruteData.P_BRUTE).value;

            var str = String.format("%s [ %s ] Brute = %s", StringUtils.rightPad(fIp.toString(), 15), StringUtils
                    .leftPad(fUser.toString(), 20), StringUtils.leftPad(fBrute.toString(), 7));

            put(am, (i == 0) ? name : "", str);
            databaseService.updateScanIp(ip.getIp(), ip.getUserName(), null);
        }
        if (ips.isEmpty()) {
            put(am, name, "<none>");
        }
    }

    private void convertLeaderboardData(AsciiMaker am, FieldData data) {
        var lbs = (List<LeaderboardData>) data.value;
        var name = data.name;

        for (var i = 0; i < lbs.size(); i++) {
            var lb = lbs.get(i);
            var str = String.format("%s. | %s %s %s", StringUtils.leftPad(String.valueOf(i + 1), 3), StringUtils
                    .rightPad(lb.getLevel().toString(), 4), StringUtils.rightPad(lb.getUser(), 25), StringUtils
                    .leftPad(lb.getExpPercent(), 6));

            put(am, (i == 0) ? name : "", str);
        }
    }

    private void convertTournament24HData(AsciiMaker am, FieldData data, long leftTime) {
        var tournaments = (List<Tournament24HData>) data.value;
        var name = data.name;

        for (var i = 0; i < tournaments.size(); i++) {
            var t = tournaments.get(i);
            var perSecond = String.format("%.2f/s", t.getExpGain() / (86400. - leftTime));
            var str = String.format("%s. | %s %s %s | %s", StringUtils.leftPad(String.valueOf(i + 1), 3), StringUtils
                    .rightPad(t.getLevel().toString(), 4), StringUtils.rightPad(t.getUser(), 25), StringUtils
                    .leftPad(t.getExpGain().toString(), 9), StringUtils.leftPad(perSecond, 8));

            put(am, (i == 0) ? name : "", str);
        }
    }

    private void convertLeaderboardCrewData(AsciiMaker am, FieldData data) {
        var crews = (List<LeaderboardCrewData>) data.value;
        var name = data.name;

        for (var i = 0; i < crews.size(); i++) {
            var crew = crews.get(i);
            var str = String.format("%s. | %s | %s | %s | %s | ID = %s", StringUtils
                    .leftPad(String.valueOf(i + 1), 3), StringUtils.rightPad(crew.getCrewTag(), 5), StringUtils
                    .rightPad(crew.getCrewName(), 25), StringUtils
                    .leftPad(String.format("%s/%s", crew.getMembers(), LeaderboardCrewData.MAX_MEMBERS), 5), StringUtils
                    .rightPad(crew.getCrewReputation(), 4), StringUtils.rightPad(crew.getCrewId().toString(), 8));

            put(am, (i == 0) ? name : "", str);
        }
    }
}
