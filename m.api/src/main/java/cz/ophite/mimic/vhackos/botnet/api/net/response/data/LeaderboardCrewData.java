package cz.ophite.mimic.vhackos.botnet.api.net.response.data;

import cz.ophite.mimic.vhackos.botnet.api.net.response.base.ResponseKey;
import cz.ophite.mimic.vhackos.botnet.shared.ascii.AsciiRow;

/**
 * Informace o crew v leaderboard.
 *
 * @author mimic
 */
public final class LeaderboardCrewData {

    public static final int MAX_MEMBERS = 20;

    @AsciiRow("Name")
    @ResponseKey(KEY_CREW_NAME)
    private String crewName;
    public static final String P_CREW_NAME = "crewName";
    private static final String KEY_CREW_NAME = "crew_name";

    @AsciiRow("Tag")
    @ResponseKey(KEY_CREW_TAG)
    private String crewTag;
    public static final String P_CREW_TAG = "crewTag";
    private static final String KEY_CREW_TAG = "crew_tag";

    @AsciiRow("Reputation")
    @ResponseKey(KEY_CREW_REPUTATION)
    private String crewReputation;
    public static final String P_CREW_REPUTATION = "crewReputation";
    private static final String KEY_CREW_REPUTATION = "crew_rep";

    @AsciiRow("ID")
    @ResponseKey(KEY_CREW_ID)
    private Integer crewId;
    public static final String P_CREW_ID = "crewId";
    private static final String KEY_CREW_ID = "crew_id";

    @AsciiRow("Members")
    @ResponseKey(KEY_MEMBERS)
    private Integer members;
    public static final String P_MEMBERS = "members";
    private static final String KEY_MEMBERS = "members";

    public String getCrewName() {
        return crewName;
    }

    public void setCrewName(String crewName) {
        this.crewName = crewName;
    }

    public String getCrewTag() {
        return crewTag;
    }

    public void setCrewTag(String crewTag) {
        this.crewTag = crewTag;
    }

    public String getCrewReputation() {
        return crewReputation;
    }

    public void setCrewReputation(String crewReputation) {
        this.crewReputation = crewReputation;
    }

    public Integer getCrewId() {
        return crewId;
    }

    public void setCrewId(Integer crewId) {
        this.crewId = crewId;
    }

    public Integer getMembers() {
        return members;
    }

    public void setMembers(Integer members) {
        this.members = members;
    }
}
