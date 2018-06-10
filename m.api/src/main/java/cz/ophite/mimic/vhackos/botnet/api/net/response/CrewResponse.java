package cz.ophite.mimic.vhackos.botnet.api.net.response;

import cz.ophite.mimic.vhackos.botnet.api.net.response.base.Response;
import cz.ophite.mimic.vhackos.botnet.api.net.response.base.ResponseKey;
import cz.ophite.mimic.vhackos.botnet.api.net.response.data.CrewMemberData;
import cz.ophite.mimic.vhackos.botnet.api.net.response.data.CrewMessageData;
import cz.ophite.mimic.vhackos.botnet.shared.utils.ascii.AsciiRow;
import cz.ophite.mimic.vhackos.botnet.shared.utils.ascii.converter.AsciiBooleanConverter;
import cz.ophite.mimic.vhackos.botnet.shared.utils.ascii.converter.AsciiElapsedTimeConverter;

import java.util.List;

/**
 * Informace o crew.
 *
 * @author mimic
 */
public final class CrewResponse extends Response {

    @AsciiRow(value = "Created", converter = AsciiBooleanConverter.class)
    @ResponseKey("created")
    private Integer created;
    public static final String P_CREATED = "created";

    @AsciiRow(value = "Requested", converter = AsciiBooleanConverter.class)
    @ResponseKey("requested")
    private Integer requested;
    public static final String P_REQUESTED = "requested";

    @AsciiRow(value = "Accepted", converter = AsciiBooleanConverter.class)
    @ResponseKey("accepted")
    private Integer accepted;
    public static final String P_ACCEPTED = "accepted";

    @AsciiRow(value = "Sent", converter = AsciiBooleanConverter.class)
    @ResponseKey("sent")
    private Integer sent;
    public static final String P_SENT = "sent";

    @AsciiRow(value = "Change Logo", converter = AsciiBooleanConverter.class)
    @ResponseKey("chgLogo")
    private Integer changeLogo;
    public static final String P_CHANGE_LOGO = "changeLogo";

    @AsciiRow(value = "Promote", converter = AsciiBooleanConverter.class)
    @ResponseKey("promote")
    private Integer promote;
    public static final String P_PROMOTE = "promote";

    @AsciiRow(value = "Demote", converter = AsciiBooleanConverter.class)
    @ResponseKey("demote")
    private Integer demote;
    public static final String P_DEMOTE = "demote";

    @AsciiRow(value = "Leave", converter = AsciiBooleanConverter.class)
    @ResponseKey("leave")
    private Integer leave;
    public static final String P_LEAVE = "leave";

    @AsciiRow(value = "Kicked", converter = AsciiBooleanConverter.class)
    @ResponseKey("kicked")
    private Integer kicked;
    public static final String P_KICKED = "kicked";

    @AsciiRow("Crew Messages")
    @ResponseKey("crew_messages")
    private Integer crewMessages;
    public static final String P_CREW_MESSAGES = "crewMessages";

    @AsciiRow("My Position")
    @ResponseKey("myposition")
    private Integer myPosition;
    public static final String P_MY_POSITION = "myPosition";

    @AsciiRow("Crew Name")
    @ResponseKey("crew_name")
    private String crewName;
    public static final String P_CREW_NAME = "crewName";

    @AsciiRow("Crew Tag")
    @ResponseKey("crew_tag")
    private String crewTag;
    public static final String P_CREW_TAG = "crewTag";

    @AsciiRow(value = "Is Member", converter = AsciiBooleanConverter.class)
    @ResponseKey("ismember")
    private Integer isMember;
    public static final String P_IS_MEMBER = "isMember";

    @AsciiRow("Crew ID")
    @ResponseKey("crew_id")
    private Long crewId;
    public static final String P_CREW_ID = "crewId";

    @AsciiRow("Crew Logo")
    @ResponseKey("crew_logo")
    private String crewLogo;
    public static final String P_CREW_LOGO = "crewLogo";

    @AsciiRow("Crew Owner")
    @ResponseKey("crew_owner")
    private String crewOwner;
    public static final String P_CREW_OWNER = "crewOwner";

    @AsciiRow("Crew Members Count")
    @ResponseKey("crew_member_count")
    private Integer crewMembersCount;
    public static final String P_CREW_MEMBERS_COUNT = "crewMembersCount";

    @AsciiRow("Crew Members")
    @ResponseKey("crew_members")
    private String crewMembers;
    public static final String P_CREW_MEMBERS = "crewMembers";

    @AsciiRow("Crew Reputation")
    @ResponseKey("crew_rep")
    private Integer crewReputation;
    public static final String P_CREW_REPUTATION = "crewReputation";

    @AsciiRow("Crew Rank")
    @ResponseKey("crew_rank")
    private Integer crewRank;
    public static final String P_CREW_RANK = "crewRank";

    @AsciiRow("Request Count")
    @ResponseKey("req_count")
    private Integer reqCount;
    public static final String P_REQ_COUNT = "reqCount";

    @AsciiRow("Crew New Messages")
    @ResponseKey("crew_new_messages")
    private Integer crewNewMessages;
    public static final String P_CREW_NEW_MESSAGES = "crewNewMessages";

    @AsciiRow("Message")
    @ResponseKey("message")
    private String message;
    public static final String P_MESSAGE = "message";

    @AsciiRow(value = "Time", converter = AsciiElapsedTimeConverter.class)
    @ResponseKey("time")
    private Long time;
    public static final String P_TIME = "time";

    @AsciiRow("User ID")
    @ResponseKey("user_id")
    private Long userId;
    public static final String P_USER_ID = "userId";

    @AsciiRow("User")
    @ResponseKey("username")
    private String userName;
    public static final String P_USER_NAME = "userName";

    @AsciiRow("Members")
    @ResponseKey("members")
    private List<CrewMemberData> members;
    public static final String P_MEMBERS = "members";

    @AsciiRow("Messages")
    @ResponseKey("crewmessages")
    private List<CrewMessageData> messages;
    public static final String P_MESSAGES = "messages";

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getCreated() {
        return created;
    }

    public void setCreated(Integer created) {
        this.created = created;
    }

    public Integer getRequested() {
        return requested;
    }

    public void setRequested(Integer requested) {
        this.requested = requested;
    }

    public Integer getAccepted() {
        return accepted;
    }

    public void setAccepted(Integer accepted) {
        this.accepted = accepted;
    }

    public Integer getSent() {
        return sent;
    }

    public void setSent(Integer sent) {
        this.sent = sent;
    }

    public Integer getChangeLogo() {
        return changeLogo;
    }

    public void setChangeLogo(Integer changeLogo) {
        this.changeLogo = changeLogo;
    }

    public Integer getPromote() {
        return promote;
    }

    public void setPromote(Integer promote) {
        this.promote = promote;
    }

    public Integer getDemote() {
        return demote;
    }

    public void setDemote(Integer demote) {
        this.demote = demote;
    }

    public Integer getLeave() {
        return leave;
    }

    public void setLeave(Integer leave) {
        this.leave = leave;
    }

    public Integer getKicked() {
        return kicked;
    }

    public void setKicked(Integer kicked) {
        this.kicked = kicked;
    }

    public Integer getCrewMessages() {
        return crewMessages;
    }

    public void setCrewMessages(Integer crewMessages) {
        this.crewMessages = crewMessages;
    }

    public Integer getMyPosition() {
        return myPosition;
    }

    public void setMyPosition(Integer myPosition) {
        this.myPosition = myPosition;
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

    public Integer getIsMember() {
        return isMember;
    }

    public void setIsMember(Integer isMember) {
        this.isMember = isMember;
    }

    public Long getCrewId() {
        return crewId;
    }

    public void setCrewId(Long crewId) {
        this.crewId = crewId;
    }

    public String getCrewLogo() {
        return crewLogo;
    }

    public void setCrewLogo(String crewLogo) {
        this.crewLogo = crewLogo;
    }

    public String getCrewOwner() {
        return crewOwner;
    }

    public void setCrewOwner(String crewOwner) {
        this.crewOwner = crewOwner;
    }

    public Integer getCrewMembersCount() {
        return crewMembersCount;
    }

    public void setCrewMembersCount(Integer crewMembersCount) {
        this.crewMembersCount = crewMembersCount;
    }

    public String getCrewMembers() {
        return crewMembers;
    }

    public void setCrewMembers(String crewMembers) {
        this.crewMembers = crewMembers;
    }

    public Integer getCrewReputation() {
        return crewReputation;
    }

    public void setCrewReputation(Integer crewReputation) {
        this.crewReputation = crewReputation;
    }

    public Integer getCrewRank() {
        return crewRank;
    }

    public void setCrewRank(Integer crewRank) {
        this.crewRank = crewRank;
    }

    public Integer getReqCount() {
        return reqCount;
    }

    public void setReqCount(Integer reqCount) {
        this.reqCount = reqCount;
    }

    public Integer getCrewNewMessages() {
        return crewNewMessages;
    }

    public void setCrewNewMessages(Integer crewNewMessages) {
        this.crewNewMessages = crewNewMessages;
    }

    public List<CrewMemberData> getMembers() {
        return members;
    }

    public void setMembers(List<CrewMemberData> members) {
        this.members = members;
    }

    public List<CrewMessageData> getMessages() {
        return messages;
    }

    public void setMessages(List<CrewMessageData> messages) {
        this.messages = messages;
    }
}
