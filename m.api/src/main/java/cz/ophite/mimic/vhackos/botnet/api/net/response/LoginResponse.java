package cz.ophite.mimic.vhackos.botnet.api.net.response;

import cz.ophite.mimic.vhackos.botnet.api.net.response.base.Response;
import cz.ophite.mimic.vhackos.botnet.api.net.response.base.ResponseKey;
import cz.ophite.mimic.vhackos.botnet.shared.utils.ascii.AsciiRow;

/**
 * Odpověď serveru při přihlášení. Používá se i pro registraci uživatele.
 *
 * @author mimic
 */
public final class LoginResponse extends Response {

    // aktuální login uživatele (měl by být stejný jako v konfiguraci)
    @AsciiRow("User")
    @ResponseKey("username")
    private String userName;
    public static final String P_USER_NAME = "userName";

    // přístupový token - většinou vrací stejný (ještě se nestalo, že by vrátil jiný)
    @AsciiRow("Access Token")
    @ResponseKey("accesstoken")
    private String accessToken;
    public static final String P_ACCESS_TOKEN = "accessToken";

    // unikátní ID uživatele na serveru
    @AsciiRow("User UID")
    @ResponseKey("uid")
    private Integer uid;
    public static final String P_UID = "uid";

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }
}
