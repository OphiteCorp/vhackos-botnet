package cz.ophite.mimic.vhackos.botnet.api.exception;

/**
 * Server vrátil neplatnou odpověď.
 *
 * @author mimic
 */
public final class InvalidRequestException extends BotnetException {

    public InvalidRequestException(String resultCode, String message) {
        super(resultCode, message);
    }
}
