package cz.ophite.mimic.vhackos.botnet.api;

import cz.ophite.mimic.vhackos.botnet.api.dto.ConnectionData;
import cz.ophite.mimic.vhackos.botnet.api.net.response.LoginResponse;

/**
 * Rozhraní pro botnet.
 *
 * @author mimic
 */
public interface IBotnet {

    /**
     * Verze Botnetu.
     */
    String VERSION = "0.9.2";

    /**
     * Verze REST API pro vHackOS.
     */
    int REST_API_VERSION = 16; // 1.45+

    /**
     * Konfigurace botnetu.
     */
    IBotnetConfig getConfig();

    /**
     * Data, která drží informace o aktuálním připojení uživatele k serveru.
     */
    ConnectionData getConnectionData();

    /**
     * Akce po znovu přihlášení uživatele.
     */
    void reloginCallback(LoginResponse loginData);
}
