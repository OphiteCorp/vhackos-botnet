package cz.ophite.mimic.vhackos.botnet.api.opcode;

import cz.ophite.mimic.vhackos.botnet.api.opcode.base.Opcode;
import cz.ophite.mimic.vhackos.botnet.api.opcode.base.OpcodeTargetType;

/**
 * Přidá uživatele mezi přátelé.
 *
 * @author mimic
 */
public final class AddFriendOpcode extends Opcode {

    private static final String PARAM_USER_ID = "user_id";

    /**
     * User ID.
     */
    public void setUserId(int userId) {
        addParam(PARAM_USER_ID, String.valueOf(userId));
    }

    @Override
    public OpcodeTargetType getTarget() {
        return OpcodeTargetType.PROFILE;
    }

    @Override
    public String getOpcodeValue() {
        return "100";
    }
}
