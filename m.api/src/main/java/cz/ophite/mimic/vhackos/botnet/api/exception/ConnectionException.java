package cz.ophite.mimic.vhackos.botnet.api.exception;

/**
 * Nebylo možné se připojit k serveru.
 *
 * @author mimic
 */
public final class ConnectionException extends BotnetException {

    public ConnectionException(String message) {
        super(null, message);
    }
}
