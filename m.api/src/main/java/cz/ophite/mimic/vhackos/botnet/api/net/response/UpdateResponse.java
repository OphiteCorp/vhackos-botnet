package cz.ophite.mimic.vhackos.botnet.api.net.response;

import cz.ophite.mimic.vhackos.botnet.api.net.response.base.Response;
import cz.ophite.mimic.vhackos.botnet.api.net.response.base.ResponseKey;
import cz.ophite.mimic.vhackos.botnet.shared.utils.ascii.AsciiRow;
import cz.ophite.mimic.vhackos.botnet.shared.utils.ascii.converter.AsciiBooleanConverter;
import cz.ophite.mimic.vhackos.botnet.shared.utils.ascii.converter.AsciiDateConverter;
import cz.ophite.mimic.vhackos.botnet.shared.utils.ascii.converter.AsciiElapsedSecondsTimeConverter;
import cz.ophite.mimic.vhackos.botnet.shared.utils.ascii.converter.AsciiMoneyConverter;

/**
 * Odpověď serveru na požadavek získání dat o uživateli.
 *
 * @author mimic
 */
public final class UpdateResponse extends Response {

    // vygenerovaný přístupový token při přihlášení
    @AsciiRow("Access Token")
    @ResponseKey("accesstoken")
    private String accessToken;
    public static final String P_ACCESS_TOKEN = "accessToken";

    // unikátní ID uživatele
    @AsciiRow("User UID")
    @ResponseKey("uid")
    private Integer uid;
    public static final String P_UID = "uid";

    @AsciiRow("User")
    @ResponseKey("username")
    private String userName;
    public static final String P_USERNAME = "userName";

    @AsciiRow(value = "Expired", converter = AsciiBooleanConverter.class)
    @ResponseKey("expired")
    private Integer expired;
    public static final String P_EXPIRED = "expired";

    @AsciiRow(value = "Easter Event", converter = AsciiBooleanConverter.class)
    @ResponseKey("easterevent")
    private Integer easterEvent;
    public static final String P_EASTER_EVENT = "easterEvent";

    @AsciiRow("Blue")
    @ResponseKey("blue")
    private Byte blue;
    public static final String P_BLUE = "blue";

    @AsciiRow("Green")
    @ResponseKey("green")
    private Byte green;
    public static final String P_GREEN = "green";

    @AsciiRow("Grey")
    @ResponseKey("grey")
    private Byte grey;
    public static final String P_GREY = "grey";

    @AsciiRow("Yellow")
    @ResponseKey("yellow")
    private Byte yellow;
    public static final String P_YELLOW = "yellow";

    @AsciiRow("Orange")
    @ResponseKey("orange")
    private Byte orange;
    public static final String P_ORANGE = "orange";

    @AsciiRow("Purple")
    @ResponseKey("purple")
    private Byte purple;
    public static final String P_PURPLE = "purple";

    @AsciiRow("Red")
    @ResponseKey("red")
    private Byte red;
    public static final String P_RED = "red";

    @AsciiRow("Turkis")
    @ResponseKey("turkis")
    private Byte turkis;
    public static final String P_TURKIS = "turkis";

    @AsciiRow("White")
    @ResponseKey("white")
    private Byte white;
    public static final String P_WHITE = "white";

    @AsciiRow("Eggs")
    @ResponseKey("eggs")
    private Integer eggs;
    public static final String P_EGGS = "eggs";

    @AsciiRow("New Message")
    @ResponseKey("newmessage")
    private Integer newMessage;
    public static final String P_NEW_MESSAGE = "newMessage";

    @AsciiRow("Unread Count")
    @ResponseKey("unreadCount")
    private Integer unreadCount;
    public static final String P_UNREAD_COUNT = "unreadCount";

    // počet dostupných exploitů pro útok
    @AsciiRow("Exploits")
    @ResponseKey("exploits")
    private Integer exploits;
    public static final String P_EXPLOITS = "exploits";

    @AsciiRow("Experience")
    @ResponseKey("exp")
    private Long experience;
    public static final String P_EXPERIENCE = "experience";

    @AsciiRow("Required Experience")
    @ResponseKey("expreq")
    private Long requiredExperience;
    public static final String P_REQUIRED_EXPERIENCE = "requiredExperience";

    @AsciiRow("ExpPC")
    @ResponseKey("exppc")
    private Long expPc;
    public static final String P_EXP_PC = "expPc";

    // počet netCoins
    @AsciiRow("NetCoins")
    @ResponseKey("netcoins")
    private Integer netCoins;
    public static final String P_NETCOINS = "netCoins";

    // úroveň uživatele
    @AsciiRow("Level")
    @ResponseKey("level")
    private Integer level;
    public static final String P_LEVEL = "level";

    // počet peněz v bance
    @AsciiRow(value = "Money", converter = AsciiMoneyConverter.class)
    @ResponseKey("money")
    private Long money;
    public static final String P_MONEY = "money";

    // aktuální IP adresa hráče (herní)
    @AsciiRow("IP")
    @ResponseKey("ipaddress")
    private String ip;
    public static final String P_IP = "ip";

    // úroveň FW
    @AsciiRow("Firewall")
    @ResponseKey("fw")
    private Integer appFirewall;
    public static final String P_APP_FIREWALL = "appFirewall";

    // úroveň AV
    @AsciiRow("Antivirus")
    @ResponseKey("av")
    private Integer appAntivirus;
    public static final String P_APP_ANTIVIRUS = "appAntivirus";

    // úroveň SDK
    @AsciiRow("SDK")
    @ResponseKey("sdk")
    private Integer appSdk;
    public static final String P_APP_SDK = "appSdk";

    @AsciiRow("CColor")
    @ResponseKey("ccolor")
    private Integer cColor;
    public static final String P_C_COLOR = "cColor";

    // úroveň BF
    @AsciiRow("BruteForce")
    @ResponseKey("brute")
    private Integer appBruteforce;
    public static final String P_APP_BRUTEFORCE = "appBruteforce";

    // úroveň SPAMu
    @AsciiRow("Spam")
    @ResponseKey("spam")
    private Integer appSpam;
    public static final String P_APP_SPAM = "appSpam";

    // počet malware kitů
    @AsciiRow(value = "Malware Kit", converter = AsciiBooleanConverter.class)
    @ResponseKey("mwk")
    private Integer malwareKit;
    public static final String P_MALWARE_KIT = "malwareKit";

    // má uživatel oprávnění moderátora
    @AsciiRow(value = "Moderator", converter = AsciiBooleanConverter.class)
    @ResponseKey("mod")
    private Integer moderator;
    public static final String P_MODERATOR = "moderator";

    @AsciiRow(value = "Crew", converter = AsciiBooleanConverter.class)
    @ResponseKey("crew")
    private Integer crew;
    public static final String P_CREW = "crew";

    @AsciiRow(value = "Miner", converter = AsciiBooleanConverter.class)
    @ResponseKey("miner")
    private Integer miner;
    public static final String P_MINER = "miner";

    // aktuální čas serveru (unixtime)
    @AsciiRow(value = "Time", converter = AsciiDateConverter.class)
    @ResponseKey("time")
    private Long time;
    public static final String P_TIME = "time";

    @AsciiRow(value = "Server", converter = AsciiBooleanConverter.class)
    @ResponseKey("server")
    private Integer server;
    public static final String P_SERVER = "server";

    @AsciiRow(value = "Miner Left", converter = AsciiElapsedSecondsTimeConverter.class)
    @ResponseKey("minerLeft")
    private Long minerLeft;
    public static final String P_MINER_LEFT = "minerLeft";

    // má uživatel BAN pro chat
    @AsciiRow(value = "Chat BAN", converter = AsciiBooleanConverter.class)
    @ResponseKey("chatban")
    private Integer chatBan;
    public static final String P_CHAT_BAN = "chatBan";

    @AsciiRow("Com Count")
    @ResponseKey("comCount")
    private Integer comCount;
    public static final String P_COM_COUNT = "comCount";

    // měla by být úroveň VIP členství (běžný hráči mají 0)
    @AsciiRow(value = "VIP", converter = AsciiBooleanConverter.class)
    @ResponseKey("vip")
    private Integer vip;
    public static final String P_VIP = "vip";

    @AsciiRow("Crew Messages")
    @ResponseKey("crewMsgCount")
    private Integer crewMsgCount;
    public static final String P_CREW_MSG_COUNT = "crewMsgCount";

    @AsciiRow(value = "Notepad", converter = AsciiBooleanConverter.class)
    @ResponseKey("notepad")
    private Integer notepad;
    public static final String P_NOTEPAD = "notepad";

    @AsciiRow(value = "Leaderboard", converter = AsciiBooleanConverter.class)
    @ResponseKey("lb")
    private Integer leaderboard;
    public static final String P_LEADERBOARD = "leaderboard";

    @AsciiRow(value = "Missions", converter = AsciiBooleanConverter.class)
    @ResponseKey("missions")
    private Integer missions;
    public static final String P_MISSIONS = "missions";

    @AsciiRow(value = "Jobs", converter = AsciiBooleanConverter.class)
    @ResponseKey("jobs")
    private Integer jobs;
    public static final String P_JOBS = "jobs";

    @AsciiRow(value = "Community", converter = AsciiBooleanConverter.class)
    @ResponseKey("community")
    private Integer community;
    public static final String P_COMMUNITY = "community";

    @AsciiRow("Running Tasks")
    @ResponseKey("taskfinish")
    private Integer runningTasks;
    public static final String P_RUNNING_TASKS = "runningTasks";

    // název aktuálního zakoupeného internetového připojení ve hře
    @AsciiRow("Internet Connection")
    @ResponseKey("inet")
    private String internetConnection;
    public static final String P_INTERNET_CONNECTION = "internetConnection";

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public Integer getExpired() {
        return expired;
    }

    public void setExpired(Integer expired) {
        this.expired = expired;
    }

    public Integer getEasterEvent() {
        return easterEvent;
    }

    public void setEasterEvent(Integer easterEvent) {
        this.easterEvent = easterEvent;
    }

    public Byte getBlue() {
        return blue;
    }

    public void setBlue(Byte blue) {
        this.blue = blue;
    }

    public Byte getGreen() {
        return green;
    }

    public void setGreen(Byte green) {
        this.green = green;
    }

    public Byte getGrey() {
        return grey;
    }

    public void setGrey(Byte grey) {
        this.grey = grey;
    }

    public Byte getYellow() {
        return yellow;
    }

    public void setYellow(Byte yellow) {
        this.yellow = yellow;
    }

    public Byte getOrange() {
        return orange;
    }

    public void setOrange(Byte orange) {
        this.orange = orange;
    }

    public Byte getPurple() {
        return purple;
    }

    public void setPurple(Byte purple) {
        this.purple = purple;
    }

    public Byte getRed() {
        return red;
    }

    public void setRed(Byte red) {
        this.red = red;
    }

    public Byte getTurkis() {
        return turkis;
    }

    public void setTurkis(Byte turkis) {
        this.turkis = turkis;
    }

    public Byte getWhite() {
        return white;
    }

    public void setWhite(Byte white) {
        this.white = white;
    }

    public Integer getEggs() {
        return eggs;
    }

    public void setEggs(Integer eggs) {
        this.eggs = eggs;
    }

    public Integer getNewMessage() {
        return newMessage;
    }

    public void setNewMessage(Integer newMessage) {
        this.newMessage = newMessage;
    }

    public Integer getUnreadCount() {
        return unreadCount;
    }

    public void setUnreadCount(Integer unreadCount) {
        this.unreadCount = unreadCount;
    }

    public Integer getExploits() {
        return exploits;
    }

    public void setExploits(Integer exploits) {
        this.exploits = exploits;
    }

    public Long getExperience() {
        return experience;
    }

    public void setExperience(Long experience) {
        this.experience = experience;
    }

    public Long getRequiredExperience() {
        return requiredExperience;
    }

    public void setRequiredExperience(Long requiredExperience) {
        this.requiredExperience = requiredExperience;
    }

    public Long getExpPc() {
        return expPc;
    }

    public void setExpPc(Long expPc) {
        this.expPc = expPc;
    }

    public Integer getNetCoins() {
        return netCoins;
    }

    public void setNetCoins(Integer netCoins) {
        this.netCoins = netCoins;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Long getMoney() {
        return money;
    }

    public void setMoney(Long money) {
        this.money = money;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Integer getAppFirewall() {
        return appFirewall;
    }

    public void setAppFirewall(Integer appFirewall) {
        this.appFirewall = appFirewall;
    }

    public Integer getAppAntivirus() {
        return appAntivirus;
    }

    public void setAppAntivirus(Integer appAntivirus) {
        this.appAntivirus = appAntivirus;
    }

    public Integer getAppSdk() {
        return appSdk;
    }

    public void setAppSdk(Integer appSdk) {
        this.appSdk = appSdk;
    }

    public Integer getcColor() {
        return cColor;
    }

    public void setcColor(Integer cColor) {
        this.cColor = cColor;
    }

    public Integer getAppBruteforce() {
        return appBruteforce;
    }

    public void setAppBruteforce(Integer appBruteforce) {
        this.appBruteforce = appBruteforce;
    }

    public Integer getAppSpam() {
        return appSpam;
    }

    public void setAppSpam(Integer appSpam) {
        this.appSpam = appSpam;
    }

    public Integer getMalwareKit() {
        return malwareKit;
    }

    public void setMalwareKit(Integer malwareKit) {
        this.malwareKit = malwareKit;
    }

    public Integer getModerator() {
        return moderator;
    }

    public void setModerator(Integer moderator) {
        this.moderator = moderator;
    }

    public Integer getCrew() {
        return crew;
    }

    public void setCrew(Integer crew) {
        this.crew = crew;
    }

    public Integer getMiner() {
        return miner;
    }

    public void setMiner(Integer miner) {
        this.miner = miner;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public Integer getServer() {
        return server;
    }

    public void setServer(Integer server) {
        this.server = server;
    }

    public Long getMinerLeft() {
        return minerLeft;
    }

    public void setMinerLeft(Long minerLeft) {
        this.minerLeft = minerLeft;
    }

    public Integer getChatBan() {
        return chatBan;
    }

    public void setChatBan(Integer chatBan) {
        this.chatBan = chatBan;
    }

    public Integer getComCount() {
        return comCount;
    }

    public void setComCount(Integer comCount) {
        this.comCount = comCount;
    }

    public Integer getVip() {
        return vip;
    }

    public void setVip(Integer vip) {
        this.vip = vip;
    }

    public Integer getCrewMsgCount() {
        return crewMsgCount;
    }

    public void setCrewMsgCount(Integer crewMsgCount) {
        this.crewMsgCount = crewMsgCount;
    }

    public Integer getNotepad() {
        return notepad;
    }

    public void setNotepad(Integer notepad) {
        this.notepad = notepad;
    }

    public Integer getLeaderboard() {
        return leaderboard;
    }

    public void setLeaderboard(Integer leaderboard) {
        this.leaderboard = leaderboard;
    }

    public Integer getMissions() {
        return missions;
    }

    public void setMissions(Integer missions) {
        this.missions = missions;
    }

    public Integer getJobs() {
        return jobs;
    }

    public void setJobs(Integer jobs) {
        this.jobs = jobs;
    }

    public Integer getCommunity() {
        return community;
    }

    public void setCommunity(Integer community) {
        this.community = community;
    }

    public Integer getRunningTasks() {
        return runningTasks;
    }

    public void setRunningTasks(Integer runningTasks) {
        this.runningTasks = runningTasks;
    }

    public String getInternetConnection() {
        return internetConnection;
    }

    public void setInternetConnection(String internetConnection) {
        this.internetConnection = internetConnection;
    }
}
