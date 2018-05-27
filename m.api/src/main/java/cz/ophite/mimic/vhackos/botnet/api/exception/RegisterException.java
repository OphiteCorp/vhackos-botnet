package cz.ophite.mimic.vhackos.botnet.api.exception;

/**
 * Nastala chyba při registraci uživatele.
 *
 * @author mimic
 */
public final class RegisterException extends BotnetException {

    public RegisterException(String resultCode, String message) {
        super(resultCode, message);
    }
}
