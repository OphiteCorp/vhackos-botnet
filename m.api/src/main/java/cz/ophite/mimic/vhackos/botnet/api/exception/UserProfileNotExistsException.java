package cz.ophite.mimic.vhackos.botnet.api.exception;

/**
 * Profil uživatele neexistuje.
 *
 * @author mimic
 */
public final class UserProfileNotExistsException extends BotnetException {

    public UserProfileNotExistsException(String resultCode, String message) {
        super(resultCode, message);
    }
}
