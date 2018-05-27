package cz.ophite.mimic.vhackos.botnet.api.net.response;

import cz.ophite.mimic.vhackos.botnet.api.net.response.base.Response;
import cz.ophite.mimic.vhackos.botnet.api.net.response.base.ResponseKey;
import cz.ophite.mimic.vhackos.botnet.shared.utils.ascii.AsciiRow;
import cz.ophite.mimic.vhackos.botnet.shared.utils.ascii.converter.AsciiBooleanConverter;
import cz.ophite.mimic.vhackos.botnet.shared.utils.ascii.converter.AsciiElapsedSecondsTimeConverter;

/**
 * Informace o uživateli.
 *
 * @author mimic
 */
public final class UserProfileResponse extends Response {

    @AsciiRow("Requested")
    @ResponseKey("requested")
    private Integer requested;
    public static final String P_REQUESTED = "requested";

    @AsciiRow("Level")
    @ResponseKey("level")
    private Integer level;
    public static final String P_LEVEL = "level";

    @AsciiRow(value = "Registration Time", converter = AsciiElapsedSecondsTimeConverter.class)
    @ResponseKey("regtime")
    private Long registrationTime;
    public static final String P_REGISTRATION_TIME = "registrationTime";

    @AsciiRow("Crew Name")
    @ResponseKey("crew_name")
    private String crewName;
    public static final String P_CREW_NAME = "crewName";

    @AsciiRow("Crew Tag")
    @ResponseKey("crew_tag")
    private String crewTag;
    public static final String P_CREW_TAG = "crewTag";

    @AsciiRow("Crew Logo")
    @ResponseKey("crew_logo")
    private String crewLogo;
    public static final String P_CREW_LOGO = "crewLogo";

    @AsciiRow("Crew Reputation")
    @ResponseKey("crew_rep")
    private Integer crewReputation;
    public static final String P_CREW_REPUTATION = "crewReputation";

    @AsciiRow("Crew Members")
    @ResponseKey("crew_members")
    private Integer crewMembers;
    public static final String P_CREW_MEMBERS = "crewMembers";

    @AsciiRow(value = "Got Crew", converter = AsciiBooleanConverter.class)
    @ResponseKey("gotcrew")
    private Integer gotCrew;
    public static final String P_GOT_CREW = "gotCrew";

    @AsciiRow("Friends Count")
    @ResponseKey("friendsCount")
    private Integer friendsCount;
    public static final String P_FRIENDS_COUNT = "friendsCount";

    @AsciiRow(value = "VIP", converter = AsciiBooleanConverter.class)
    @ResponseKey("vip")
    private Integer vip;
    public static final String P_VIP = "vip";

    @AsciiRow("Friends")
    @ResponseKey("friends")
    private Object friends;
    public static final String P_FRIENDS = "friends";

    public Integer getRequested() {
        return requested;
    }

    public void setRequested(Integer requested) {
        this.requested = requested;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Long getRegistrationTime() {
        return registrationTime;
    }

    public void setRegistrationTime(Long registrationTime) {
        this.registrationTime = registrationTime;
    }

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

    public String getCrewLogo() {
        return crewLogo;
    }

    public void setCrewLogo(String crewLogo) {
        this.crewLogo = crewLogo;
    }

    public Integer getCrewReputation() {
        return crewReputation;
    }

    public void setCrewReputation(Integer crewReputation) {
        this.crewReputation = crewReputation;
    }

    public Integer getCrewMembers() {
        return crewMembers;
    }

    public void setCrewMembers(Integer crewMembers) {
        this.crewMembers = crewMembers;
    }

    public Integer getGotCrew() {
        return gotCrew;
    }

    public void setGotCrew(Integer gotCrew) {
        this.gotCrew = gotCrew;
    }

    public Integer getFriendsCount() {
        return friendsCount;
    }

    public void setFriendsCount(Integer friendsCount) {
        this.friendsCount = friendsCount;
    }

    public Integer getVip() {
        return vip;
    }

    public void setVip(Integer vip) {
        this.vip = vip;
    }

    public Object getFriends() {
        return friends;
    }

    public void setFriends(Object friends) {
        this.friends = friends;
    }
}
