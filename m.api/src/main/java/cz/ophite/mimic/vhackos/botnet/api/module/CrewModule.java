package cz.ophite.mimic.vhackos.botnet.api.module;

import cz.ophite.mimic.vhackos.botnet.api.IBotnet;
import cz.ophite.mimic.vhackos.botnet.api.exception.BotnetException;
import cz.ophite.mimic.vhackos.botnet.api.exception.CrewNotExistsException;
import cz.ophite.mimic.vhackos.botnet.api.exception.WithoutCrewException;
import cz.ophite.mimic.vhackos.botnet.api.module.base.Module;
import cz.ophite.mimic.vhackos.botnet.api.module.base.ModuleHelper;
import cz.ophite.mimic.vhackos.botnet.api.net.response.CrewProfileResponse;
import cz.ophite.mimic.vhackos.botnet.api.net.response.CrewResponse;
import cz.ophite.mimic.vhackos.botnet.api.opcode.CrewLeaveOpcode;
import cz.ophite.mimic.vhackos.botnet.api.opcode.CrewOpcode;
import cz.ophite.mimic.vhackos.botnet.api.opcode.CrewProfileOpcode;
import cz.ophite.mimic.vhackos.botnet.api.opcode.CrewSendMessageOpcode;
import cz.ophite.mimic.vhackos.botnet.api.opcode.base.Opcode;
import cz.ophite.mimic.vhackos.botnet.shared.injection.Inject;
import cz.ophite.mimic.vhackos.botnet.shared.utils.SharedUtils;

import java.util.Collections;

/**
 * Správa crew.
 *
 * @author mimic
 */
@Inject
public final class CrewModule extends Module {

    private static final String ERR_CODE_CREW_NOT_EXISTS = "1";

    protected CrewModule(IBotnet botnet) {
        super(botnet);
    }

    /**
     * Získá profil crew.
     */
    public synchronized CrewProfileResponse getProfile(long crewId) {
        var opcode = new CrewProfileOpcode();
        opcode.setCrewId(crewId);

        try {
            var response = sendRequest(opcode);

            var dto = new CrewProfileResponse();
            ModuleHelper.checkResponseIntegrity(response.keySet(), CrewProfileResponse.class);
            ModuleHelper.setField(response, dto, CrewProfileResponse.P_REQUESTED);
            ModuleHelper.setField(response, dto, CrewProfileResponse.P_CREW_NAME);
            ModuleHelper.setField(response, dto, CrewProfileResponse.P_CREW_TAG);
            ModuleHelper.setField(response, dto, CrewProfileResponse.P_CREW_LOGO);
            ModuleHelper.setField(response, dto, CrewProfileResponse.P_CREW_REPUTATION);
            ModuleHelper.setField(response, dto, CrewProfileResponse.P_CREW_MEMBERS);
            ModuleHelper.setField(response, dto, CrewProfileResponse.P_CAN_REQUEST);
            return dto;

        } catch (BotnetException e) {
            if (ERR_CODE_CREW_NOT_EXISTS.equals(e.getResultCode())) {
                throw new CrewNotExistsException(e.getResultCode(), "Crew with ID '" + crewId + "' does not exist");
            }
            throw e;
        }
    }

    /**
     * Získá informace o aktuální crew.
     */
    public synchronized CrewResponse getCrew() {
        var opcode = new CrewOpcode();
        var resp = createCrewResponse(opcode);

        checkUserInAnyCrew(resp);
        return resp;
    }

    /**
     * Odešle crew zprávu.
     */
    public synchronized CrewResponse sendMessage(String message) {
        var opcode = new CrewSendMessageOpcode();
        var resp = createCrewResponse(opcode);

        checkUserInAnyCrew(resp);
        return resp;
    }

    /**
     * Opustí crew.
     */
    public synchronized CrewResponse leaveCrew() {
        var opcode = new CrewLeaveOpcode();
        var resp = createCrewResponse(opcode);

        checkUserInAnyCrew(resp);
        return resp;
    }

    private static void checkUserInAnyCrew(CrewResponse resp) {
        if (!SharedUtils.toBoolean(resp.getIsMember())) {
            throw new WithoutCrewException(null, "You are not in any crew");
        }
    }

    private CrewResponse createCrewResponse(Opcode opcode) {
        var response = sendRequest(opcode);

        var dto = new CrewResponse();
        ModuleHelper.checkResponseIntegrity(response.keySet(), CrewResponse.class);
        ModuleHelper.setField(response, dto, CrewResponse.P_CREATED);
        ModuleHelper.setField(response, dto, CrewResponse.P_REQUESTED);
        ModuleHelper.setField(response, dto, CrewResponse.P_ACCEPTED);
        ModuleHelper.setField(response, dto, CrewResponse.P_SENT);
        ModuleHelper.setField(response, dto, CrewResponse.P_CHANGE_LOGO);
        ModuleHelper.setField(response, dto, CrewResponse.P_PROMOTE);
        ModuleHelper.setField(response, dto, CrewResponse.P_DEMOTE);
        ModuleHelper.setField(response, dto, CrewResponse.P_LEAVE);
        ModuleHelper.setField(response, dto, CrewResponse.P_KICKED);
        ModuleHelper.setField(response, dto, CrewResponse.P_CREW_MESSAGES);
        ModuleHelper.setField(response, dto, CrewResponse.P_MY_POSITION);
        ModuleHelper.setField(response, dto, CrewResponse.P_CREW_NAME);
        ModuleHelper.setField(response, dto, CrewResponse.P_CREW_TAG);
        ModuleHelper.setField(response, dto, CrewResponse.P_IS_MEMBER);
        ModuleHelper.setField(response, dto, CrewResponse.P_CREW_ID);
        ModuleHelper.setField(response, dto, CrewResponse.P_CREW_LOGO);
        ModuleHelper.setField(response, dto, CrewResponse.P_CREW_OWNER);
        ModuleHelper.setField(response, dto, CrewResponse.P_CREW_MEMBERS_COUNT);
        ModuleHelper.setField(response, dto, CrewResponse.P_CREW_MEMBERS);
        ModuleHelper.setField(response, dto, CrewResponse.P_CREW_REPUTATION);
        ModuleHelper.setField(response, dto, CrewResponse.P_CREW_RANK);
        ModuleHelper.setField(response, dto, CrewResponse.P_REQ_COUNT);
        ModuleHelper.setField(response, dto, CrewResponse.P_CREW_NEW_MESSAGES);
        ModuleHelper.setField(response, dto, CrewResponse.P_MESSAGE);
        ModuleHelper.setField(response, dto, CrewResponse.P_TIME);
        ModuleHelper.setField(response, dto, CrewResponse.P_USER_ID);
        ModuleHelper.setField(response, dto, CrewResponse.P_USER_NAME);

        if (!ModuleHelper.setField(response, dto, CrewResponse.P_MEMBERS, (f, data) -> ModuleHelper
                .convertToCrewMemberData(data))) {
            dto.setMembers(Collections.emptyList());
        }
        if (!ModuleHelper.setField(response, dto, CrewResponse.P_MESSAGES, (f, data) -> ModuleHelper
                .convertToCrewMessageData(data))) {
            dto.setMessages(Collections.emptyList());
        }
        return dto;
    }
}
