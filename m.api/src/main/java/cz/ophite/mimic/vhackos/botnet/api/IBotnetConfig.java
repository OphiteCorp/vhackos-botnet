package cz.ophite.mimic.vhackos.botnet.api;

import cz.ophite.mimic.vhackos.botnet.api.dto.ProxyData;

/**
 * Konfigurace pro botnet API.
 *
 * @author mimic
 */
public interface IBotnetConfig {

    /**
     * Login uživatele.
     */
    String getUserName();

    /**
     * Heslo uživatele.
     */
    String getPassword();

    /**
     * Zpráva, která se má zanechat v logu oběti.
     */
    String getMessageLog();

    /**
     * Maximální počet pokusů o navázání připojení.
     */
    int getMaxRequestAttempts();

    /**
     * Prodleva pro uspání vlákna.
     */
    long getSleepDelay();

    /**
     * Získá informace o proxy serveru.
     */
    ProxyData getProxyData();

    /**
     * Je povolen agresivní mód?
     */
    boolean isAggressiveMode();

    /**
     * Prodleva pro odeslání a čtení požadavku.
     */
    int getConnectionTimeout();
}
