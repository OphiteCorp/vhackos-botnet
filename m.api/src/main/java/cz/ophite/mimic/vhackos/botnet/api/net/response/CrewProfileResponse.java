package cz.ophite.mimic.vhackos.botnet.api.net.response;

import cz.ophite.mimic.vhackos.botnet.api.net.response.base.Response;
import cz.ophite.mimic.vhackos.botnet.api.net.response.base.ResponseKey;
import cz.ophite.mimic.vhackos.botnet.shared.utils.ascii.AsciiRow;
import cz.ophite.mimic.vhackos.botnet.shared.utils.ascii.converter.AsciiBooleanConverter;

/**
 * Informace o crew.
 *
 * @author mimic
 */
public final class CrewProfileResponse extends Response {

    @AsciiRow(value = "Requested", converter = AsciiBooleanConverter.class)
    @ResponseKey("requested")
    private Integer requested;
    public static final String P_REQUESTED = "requested";

    @AsciiRow("Name")
    @ResponseKey("crew_name")
    private String crewName;
    public static final String P_CREW_NAME = "crewName";

    @AsciiRow("Tag")
    @ResponseKey("crew_tag")
    private String crewTag;
    public static final String P_CREW_TAG = "crewTag";

    @AsciiRow("Logo")
    @ResponseKey("crew_logo")
    private String crewLogo;
    public static final String P_CREW_LOGO = "crewLogo";

    @AsciiRow("Reputation")
    @ResponseKey("crew_rep")
    private Integer crewReputation;
    public static final String P_CREW_REPUTATION = "crewReputation";

    @AsciiRow("Members")
    @ResponseKey("crew_members")
    private Integer crewMembers;
    public static final String P_CREW_MEMBERS = "crewMembers";

    @AsciiRow(value = "Can Request", converter = AsciiBooleanConverter.class)
    @ResponseKey("can_request")
    private Integer canRequest;
    public static final String P_CAN_REQUEST = "canRequest";

    public Integer getRequested() {
        return requested;
    }

    public void setRequested(Integer requested) {
        this.requested = requested;
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

    public Integer getCanRequest() {
        return canRequest;
    }

    public void setCanRequest(Integer canRequest) {
        this.canRequest = canRequest;
    }
}
