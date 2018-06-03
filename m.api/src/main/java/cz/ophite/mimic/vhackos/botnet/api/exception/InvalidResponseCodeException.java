package cz.ophite.mimic.vhackos.botnet.api.exception;

/**
 * Server vrátil neplatný kód odpovědi.
 *
 * @author mimic
 */
public final class InvalidResponseCodeException extends BotnetException {

    private int responseCode;

    public InvalidResponseCodeException(int responseCode, String message) {
        super(null, message);
        this.responseCode = responseCode;
    }

    public int getResponseCode() {
        return responseCode;
    }
}
