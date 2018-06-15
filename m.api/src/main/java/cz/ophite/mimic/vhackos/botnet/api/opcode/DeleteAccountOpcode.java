package cz.ophite.mimic.vhackos.botnet.api.opcode;

import cz.ophite.mimic.vhackos.botnet.api.opcode.base.Opcode;
import cz.ophite.mimic.vhackos.botnet.api.opcode.base.OpcodeTargetType;

/**
 * Umožní smazat účet uživatele.
 *
 * @author mimic
 */
public final class DeleteAccountOpcode extends Opcode {

    private static final String PARAM_UID = "uid";
    private static final String PARAM_ACCESS_TOKEN = "accesstoken";
    private static final String PARAM_LANGUAGE = "lang";
    private static final String PARAM_PASSWORD = "password2";

    /**
     * Unikátní UID uživatele.
     */
    public void setUid(String uid) {
        addParam(PARAM_UID, uid);
    }

    /**
     * Přístupový token uživatele.
     */
    public void setAccessToken(String accessToken) {
        addParam(PARAM_ACCESS_TOKEN, accessToken);
    }

    /**
     * Kód jazyku v ISO2.
     */
    public void setLanguage(String langCodeIso2) {
        addParam(PARAM_LANGUAGE, langCodeIso2);
    }

    /**
     * Ověřovací heslo v MD5.
     */
    public void setPasswordHash(String md5PasswordHash) {
        addParam(PARAM_PASSWORD, md5PasswordHash);
    }

    @Override
    public OpcodeTargetType getTarget() {
        return OpcodeTargetType.DELETE_ACCOUNT;
    }

    @Override
    public boolean isStandaloneOpcode() {
        return true;
    }
}
