package cz.ophite.mimic.vhackos.botnet.api.exception;

/**
 * Byl zadán neplatný přístupový token do požadavku.
 *
 * @author mimic
 */
public final class InvalidAccessTokenException extends BotnetException {

    public InvalidAccessTokenException(String resultCode, String message) {
        super(resultCode, message);
    }
}
