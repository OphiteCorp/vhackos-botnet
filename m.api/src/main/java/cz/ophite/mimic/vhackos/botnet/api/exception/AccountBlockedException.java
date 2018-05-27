package cz.ophite.mimic.vhackos.botnet.api.exception;

/**
 * Účet uživatele byl zablokován.
 *
 * @author mimic
 */
public final class AccountBlockedException extends BotnetException {

    public AccountBlockedException(String resultCode, String message) {
        super(resultCode, message);
    }
}
