package cz.ophite.mimic.vhackos.botnet.api.net.response;

import cz.ophite.mimic.vhackos.botnet.api.net.response.base.Response;
import cz.ophite.mimic.vhackos.botnet.api.net.response.base.ResponseKey;
import cz.ophite.mimic.vhackos.botnet.shared.utils.ascii.AsciiRow;
import cz.ophite.mimic.vhackos.botnet.shared.utils.ascii.converter.AsciiBooleanConverter;

/**
 * Informace o vzdáleném systému.
 *
 * @author mimic
 */
public final class RemoteSystemResponse extends Response {

    @AsciiRow("Custom Background")
    @ResponseKey("customBG")
    private String customBackground;
    public static final String P_CUSTOM_BACKGROUND = "customBackground";

    @AsciiRow("IP")
    @ResponseKey("remoteIP")
    private String ip;
    public static final String P_IP = "ip";

    @AsciiRow("Remote IP")
    @ResponseKey("remote_ip")
    private String remoteIp;
    public static final String P_REMOTE_IP = "remoteIp";

    @AsciiRow("Level")
    @ResponseKey("remoteLevel")
    private Integer level;
    public static final String P_LEVEL = "level";

    @AsciiRow("User")
    @ResponseKey("remoteUsername")
    private String userName;
    public static final String P_USER_NAME = "userName";

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

    @AsciiRow("Internet Connection")
    @ResponseKey("remoteConnection")
    private String internetConnection;
    public static final String P_INTERNET_CONNECTION = "internetConnection";

    @AsciiRow("Remote Crew")
    @ResponseKey("remoteCrew")
    private String remoteCrew;
    public static final String P_REMOTE_CREW = "remoteCrew";

    public String getRemoteCrew() {
        return remoteCrew;
    }

    public void setRemoteCrew(String remoteCrew) {
        this.remoteCrew = remoteCrew;
    }

    public String getRemoteIp() {
        return remoteIp;
    }

    public void setRemoteIp(String remoteIp) {
        this.remoteIp = remoteIp;
    }

    public String getCustomBackground() {
        return customBackground;
    }

    public void setCustomBackground(String customBackground) {
        this.customBackground = customBackground;
    }

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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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

    public String getInternetConnection() {
        return internetConnection;
    }

    public void setInternetConnection(String internetConnection) {
        this.internetConnection = internetConnection;
    }
}
