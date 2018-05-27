package cz.ophite.mimic.vhackos.botnet.api.dto;

import com.google.gson.annotations.SerializedName;
import cz.ophite.mimic.vhackos.botnet.shared.injection.Inject;

/**
 * Data, která slouží pro připojení uživatele na vHackOS server.
 *
 * @author mimic
 */
@Inject
public final class ConnectionData {

    @SerializedName("user_name")
    private String userName;

    @SerializedName("access_token")
    private String accessToken;

    @SerializedName("uid")
    private Integer uid;

    @SerializedName("language")
    private String lang;

    @SerializedName("user_agent")
    private String userAgent;

    public void set(ConnectionData data) {
        userName = data.getUserName();
        accessToken = data.getAccessToken();
        uid = data.getUid();
        lang = data.getLang();
        userAgent = data.getUserAgent();
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
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

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }
}
