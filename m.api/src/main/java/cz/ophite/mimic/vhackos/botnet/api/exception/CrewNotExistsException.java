package cz.ophite.mimic.vhackos.botnet.api.exception;

/**
 * Crew neexistuje.
 *
 * @author mimic
 */
public final class CrewNotExistsException extends BotnetException {

    public CrewNotExistsException(String resultCode, String message) {
        super(resultCode, message);
    }
}
