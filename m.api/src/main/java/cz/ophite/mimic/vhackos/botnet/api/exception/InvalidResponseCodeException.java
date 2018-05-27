package cz.ophite.mimic.vhackos.botnet.api.exception;

/**
 * Server vrátil neplatný kód odpovědi.
 *
 * @author mimic
 */
public final class InvalidResponseCodeException extends BotnetException {

    public InvalidResponseCodeException(String resultCode, String message) {
        super(resultCode, message);
    }
}
