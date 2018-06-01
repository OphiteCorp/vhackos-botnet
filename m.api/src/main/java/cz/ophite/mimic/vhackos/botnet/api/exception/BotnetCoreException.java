package cz.ophite.mimic.vhackos.botnet.api.exception;

/**
 * Hlavní vyjímka pro botnet.
 *
 * @author mimic
 */
public class BotnetCoreException extends RuntimeException {

    public BotnetCoreException() {
        super();
    }

    BotnetCoreException(String message) {
        super(message);
    }

    public BotnetCoreException(String message, Throwable cause) {
        super(message, cause);
    }

    public BotnetCoreException(Throwable cause) {
        super(cause);
    }

    protected BotnetCoreException(String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
