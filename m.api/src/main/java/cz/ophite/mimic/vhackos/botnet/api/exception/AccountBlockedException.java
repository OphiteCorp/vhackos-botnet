package cz.ophite.mimic.vhackos.botnet.api.exception;

/**
 * Účet uživatele byl zablokován.
 *
 * @author mimic
 */
public final class AccountBlockedException extends BotnetException {

    private final String userName;

    public AccountBlockedException(String resultCode, String userName, String message) {
        super(resultCode, message);
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }
}
