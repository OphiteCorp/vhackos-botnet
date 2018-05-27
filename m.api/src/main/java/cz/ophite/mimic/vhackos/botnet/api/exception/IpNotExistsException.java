package cz.ophite.mimic.vhackos.botnet.api.exception;

/**
 * IP uživatele již neexistuje.
 *
 * @author mimic
 */
public final class IpNotExistsException extends BotnetException {

    public IpNotExistsException(String resultCode, String message) {
        super(resultCode, message);
    }
}
