package cz.ophite.mimic.vhackos.botnet.api;

import cz.ophite.mimic.vhackos.botnet.api.dto.ConnectionData;
import cz.ophite.mimic.vhackos.botnet.api.net.response.LoginResponse;
import cz.ophite.mimic.vhackos.botnet.shared.utils.Version;

/**
 * Rozhraní pro botnet.
 *
 * @author mimic
 */
public interface IBotnet {

    /**
     * Verze Botnetu.
     */
    Version VERSION = Version.create("0.9.7");

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
