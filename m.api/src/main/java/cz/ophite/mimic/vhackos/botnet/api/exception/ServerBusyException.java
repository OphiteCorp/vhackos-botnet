package cz.ophite.mimic.vhackos.botnet.api.exception;

/**
 * Server je zaneprázdněn.
 *
 * @author mimic
 */
public final class ServerBusyException extends BotnetException {

    public ServerBusyException(String resultCode, String message) {
        super(resultCode, message);
    }
}
