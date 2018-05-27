package cz.ophite.mimic.vhackos.botnet.api.opcode;

import cz.ophite.mimic.vhackos.botnet.api.opcode.base.Opcode;
import cz.ophite.mimic.vhackos.botnet.api.opcode.base.OpcodeTargetType;

/**
 * Zaregistruje nového uživatele.
 *
 * @author mimic
 */
public final class RegisterOpcode extends Opcode {

    private static final String PARAM_USERNAME = "username";
    private static final String PARAM_PASSWORD = "password";
    private static final String PARAM_EMAIL = "email";
    private static final String PARAM_LANGUAGE = "lang";

    /**
     * Uživatelské jméno.
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

    /**
     * Email.
     */
    public void setEmail(String email) {
        addParam(PARAM_EMAIL, email);
    }

    /**
     * Kód jazyku.
     */
    public void setLanguage(String langCodeIso2) {
        addParam(PARAM_LANGUAGE, langCodeIso2);
    }

    @Override
    public OpcodeTargetType getTarget() {
        return OpcodeTargetType.REGISTER;
    }

    @Override
    public boolean isStandaloneOpcode() {
        return true;
    }
}
