package cz.ophite.mimic.vhackos.botnet.api.module;

import cz.ophite.mimic.vhackos.botnet.api.IBotnet;
import cz.ophite.mimic.vhackos.botnet.api.exception.BotnetException;
import cz.ophite.mimic.vhackos.botnet.api.exception.CrewNotExistsException;
import cz.ophite.mimic.vhackos.botnet.api.module.base.Module;
import cz.ophite.mimic.vhackos.botnet.api.module.base.ModuleHelper;
import cz.ophite.mimic.vhackos.botnet.api.net.response.CrewProfileResponse;
import cz.ophite.mimic.vhackos.botnet.api.opcode.CrewProfileOpcode;
import cz.ophite.mimic.vhackos.botnet.shared.injection.Inject;

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
}
