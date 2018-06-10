package cz.ophite.mimic.vhackos.botnet.api.exception;

/**
 * Uživatel není v žádné crew.
 *
 * @author mimic
 */
public final class WithoutCrewException extends BotnetException {

    public WithoutCrewException(String resultCode, String message) {
        super(resultCode, message);
    }
}
