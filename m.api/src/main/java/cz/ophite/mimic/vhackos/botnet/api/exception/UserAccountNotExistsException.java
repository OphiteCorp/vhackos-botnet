package cz.ophite.mimic.vhackos.botnet.api.exception;

/**
 * Uživatelský účet nebyl nalezen.
 *
 * @author mimic
 */
public final class UserAccountNotExistsException extends BotnetException {

    public UserAccountNotExistsException(String resultCode, String message) {
        super(resultCode, message);
    }
}
