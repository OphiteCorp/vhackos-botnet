package cz.ophite.mimic.vhackos.botnet.api.exception;

/**
 * Přihlášení se nezdařilo. Špatné uživatelské jméno nebo heslo.
 *
 * @author mimic
 */
public final class InvalidLoginException extends BotnetException {

    public InvalidLoginException(String resultCode, String message) {
        super(resultCode, message);
    }
}
