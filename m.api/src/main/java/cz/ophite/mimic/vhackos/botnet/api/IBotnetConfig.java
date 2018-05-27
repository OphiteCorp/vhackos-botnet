package cz.ophite.mimic.vhackos.botnet.api;

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
}
