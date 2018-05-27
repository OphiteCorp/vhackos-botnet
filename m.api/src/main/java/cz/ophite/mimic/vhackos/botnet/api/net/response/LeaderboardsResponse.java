package cz.ophite.mimic.vhackos.botnet.api.net.response;

import cz.ophite.mimic.vhackos.botnet.api.net.response.base.Response;
import cz.ophite.mimic.vhackos.botnet.api.net.response.base.ResponseKey;
import cz.ophite.mimic.vhackos.botnet.api.net.response.data.LeaderboardCrewData;
import cz.ophite.mimic.vhackos.botnet.api.net.response.data.LeaderboardData;
import cz.ophite.mimic.vhackos.botnet.api.net.response.data.Tournament24HData;
import cz.ophite.mimic.vhackos.botnet.shared.utils.ascii.AsciiRow;
import cz.ophite.mimic.vhackos.botnet.shared.utils.ascii.converter.AsciiElapsedSecondsTimeConverter;

import java.util.List;

/**
 * Informace z leaderboards.
 *
 * @author mimic
 */
public final class LeaderboardsResponse extends Response {

    @AsciiRow("Count")
    @ResponseKey("count")
    private Integer count;
    public static final String P_COUNT = "count";

    @AsciiRow("My Level")
    @ResponseKey("mylevel")
    private Integer myLevel;
    public static final String P_MY_LEVEL = "myLevel";

    @AsciiRow("My Experience")
    @ResponseKey("myexp")
    private Long myExp;
    public static final String P_MY_EXP = "myExp";

    @AsciiRow("My Required Experience")
    @ResponseKey("myexpreq")
    private Long myExpReq;
    public static final String P_MY_EXP_REQ = "myExpReq";

    @AsciiRow("Experience Gain")
    @ResponseKey("expgain")
    private Long expGain;
    public static final String P_EXP_GAIN = "expGain";

    @AsciiRow(value = "Tournament Left", converter = AsciiElapsedSecondsTimeConverter.class)
    @ResponseKey("tournamentleft")
    private Long tournamentLeft;
    public static final String P_TOURNAMENT_LEFT = "tournamentLeft";

    @AsciiRow("Tournament Rank")
    @ResponseKey("tournamentrank")
    private Integer tournamentRank;
    public static final String P_TOURNAMENT_RANK = "tournamentRank";

    @AsciiRow("My Rank")
    @ResponseKey("myrank")
    private Integer myRank;
    public static final String P_MY_RANK = "myRank";

    @AsciiRow("Crew Count")
    @ResponseKey("crewcount")
    private Integer crewCount;
    public static final String P_CREW_COUNT = "crewCount";

    @AsciiRow("Data")
    @ResponseKey("data")
    private List<LeaderboardData> leaderboardData;
    public static final String P_LEADERBOARD_DATA = "leaderboardData";

    @AsciiRow("Tournament Data")
    @ResponseKey("tm")
    private List<Tournament24HData> tournamentData;
    public static final String P_TOURNAMENT_DATA = "tournamentData";

    @AsciiRow("Crews Data")
    @ResponseKey("crews")
    private List<LeaderboardCrewData> crewsData;
    public static final String P_CREWS_DATA = "crewsData";

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Integer getMyLevel() {
        return myLevel;
    }

    public void setMyLevel(Integer myLevel) {
        this.myLevel = myLevel;
    }

    public Long getMyExp() {
        return myExp;
    }

    public void setMyExp(Long myExp) {
        this.myExp = myExp;
    }

    public Long getMyExpReq() {
        return myExpReq;
    }

    public void setMyExpReq(Long myExpReq) {
        this.myExpReq = myExpReq;
    }

    public Long getExpGain() {
        return expGain;
    }

    public void setExpGain(Long expGain) {
        this.expGain = expGain;
    }

    public Long getTournamentLeft() {
        return tournamentLeft;
    }

    public void setTournamentLeft(Long tournamentLeft) {
        this.tournamentLeft = tournamentLeft;
    }

    public Integer getTournamentRank() {
        return tournamentRank;
    }

    public void setTournamentRank(Integer tournamentRank) {
        this.tournamentRank = tournamentRank;
    }

    public Integer getMyRank() {
        return myRank;
    }

    public void setMyRank(Integer myRank) {
        this.myRank = myRank;
    }

    public Integer getCrewCount() {
        return crewCount;
    }

    public void setCrewCount(Integer crewCount) {
        this.crewCount = crewCount;
    }

    public List<LeaderboardData> getLeaderboardData() {
        return leaderboardData;
    }

    public void setLeaderboardData(List<LeaderboardData> leaderboardData) {
        this.leaderboardData = leaderboardData;
    }

    public List<Tournament24HData> getTournamentData() {
        return tournamentData;
    }

    public void setTournamentData(List<Tournament24HData> tournamentData) {
        this.tournamentData = tournamentData;
    }

    public List<LeaderboardCrewData> getCrewsData() {
        return crewsData;
    }

    public void setCrewsData(List<LeaderboardCrewData> crewsData) {
        this.crewsData = crewsData;
    }
}
