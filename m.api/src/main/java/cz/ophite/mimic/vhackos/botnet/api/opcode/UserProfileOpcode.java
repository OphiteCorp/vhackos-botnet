package cz.ophite.mimic.vhackos.botnet.api.opcode;

import cz.ophite.mimic.vhackos.botnet.api.opcode.base.Opcode;
import cz.ophite.mimic.vhackos.botnet.api.opcode.base.OpcodeTargetType;

/**
 * Získá informace o profilu uživatele.
 *
 * @author mimic
 */
public final class UserProfileOpcode extends Opcode {

    private static final String PARAM_USER_ID = "user_id";

    /**
     * ID uživatele.
     */
    public void setUserId(int userId) {
        addParam(PARAM_USER_ID, String.valueOf(userId));
    }

    @Override
    public OpcodeTargetType getTarget() {
        return OpcodeTargetType.PROFILE;
    }
}
