package cz.ophite.mimic.vhackos.botnet.api.module;

import cz.ophite.mimic.vhackos.botnet.api.IBotnet;
import cz.ophite.mimic.vhackos.botnet.api.exception.BotnetException;
import cz.ophite.mimic.vhackos.botnet.api.exception.UserProfileNotExistsException;
import cz.ophite.mimic.vhackos.botnet.api.module.base.Module;
import cz.ophite.mimic.vhackos.botnet.api.module.base.ModuleHelper;
import cz.ophite.mimic.vhackos.botnet.api.net.response.UserProfileResponse;
import cz.ophite.mimic.vhackos.botnet.api.opcode.AddFriendOpcode;
import cz.ophite.mimic.vhackos.botnet.api.opcode.UserProfileOpcode;
import cz.ophite.mimic.vhackos.botnet.api.opcode.base.Opcode;
import cz.ophite.mimic.vhackos.botnet.shared.injection.Inject;

/**
 * Informace a správa profilu uživatele.
 *
 * @author mimic
 */
@Inject
public final class ProfileModule extends Module {

    private static final String ERR_CODE_PROFILE_NOT_EXISTS = "1";

    protected ProfileModule(IBotnet botnet) {
        super(botnet);
    }

    /**
     * Získá informace o uživateli z profilu.
     */
    public synchronized UserProfileResponse getProfile(int userId) {
        var opcode = new UserProfileOpcode();
        opcode.setUserId(userId);

        return createUserProfileResponse(opcode, userId);
    }

    /**
     * Přidá uživatele mezi přátelé.
     */
    public synchronized UserProfileResponse addFriend(int userId) {
        var opcode = new AddFriendOpcode();
        opcode.setUserId(userId);

        return createUserProfileResponse(opcode, userId);
    }

    private UserProfileResponse createUserProfileResponse(Opcode opcode, int userId) {
        try {
            var response = sendRequest(opcode);
            var dto = new UserProfileResponse();

            ModuleHelper.checkResponseIntegrity(response.keySet(), UserProfileResponse.class);
            ModuleHelper.setField(response, dto, UserProfileResponse.P_REQUESTED);
            ModuleHelper.setField(response, dto, UserProfileResponse.P_LEVEL);
            ModuleHelper.setField(response, dto, UserProfileResponse.P_REGISTRATION_TIME);
            ModuleHelper.setField(response, dto, UserProfileResponse.P_CREW_NAME);
            ModuleHelper.setField(response, dto, UserProfileResponse.P_CREW_TAG);
            ModuleHelper.setField(response, dto, UserProfileResponse.P_CREW_LOGO);
            ModuleHelper.setField(response, dto, UserProfileResponse.P_CREW_REPUTATION);
            ModuleHelper.setField(response, dto, UserProfileResponse.P_CREW_MEMBERS);
            ModuleHelper.setField(response, dto, UserProfileResponse.P_GOT_CREW);
            ModuleHelper.setField(response, dto, UserProfileResponse.P_FRIENDS_COUNT);
            ModuleHelper.setField(response, dto, UserProfileResponse.P_VIP);
            ModuleHelper.setField(response, dto, UserProfileResponse.P_FRIENDS);

            return dto;

        } catch (BotnetException e) {
            if (ERR_CODE_PROFILE_NOT_EXISTS.equals(e.getResultCode())) {
                throw new UserProfileNotExistsException(e
                        .getResultCode(), "User profile with ID '" + userId + "' does not exist");
            }
            throw e;
        }
    }
}
