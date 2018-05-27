package cz.ophite.mimic.vhackos.botnet.api.opcode;

import cz.ophite.mimic.vhackos.botnet.api.opcode.base.Opcode;
import cz.ophite.mimic.vhackos.botnet.api.opcode.base.OpcodeTargetType;

/**
 * Vytvoří opcode pro přihlášení uživatele.
 *
 * @author mimic
 */
public final class LoginOpcode extends Opcode {

    private static final String PARAM_USERNAME = "username";
    private static final String PARAM_PASSWORD = "password";

    /**
     * Uživatelské přihlašovací jméno.
     */
    public void setUserName(String userName) {
        addParam(PARAM_USERNAME, userName);
    }

    /**
     * Heslo v MD5 pro přihlášení.
     */
    public void setPasswordHash(String md5PasswordHash) {
        addParam(PARAM_PASSWORD, md5PasswordHash);
    }

    @Override
    public OpcodeTargetType getTarget() {
        return OpcodeTargetType.LOGIN;
    }
}
