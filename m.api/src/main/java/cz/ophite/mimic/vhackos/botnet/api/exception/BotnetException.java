package cz.ophite.mimic.vhackos.botnet.api.exception;

/**
 * Hlavní vyjímka botnetu.
 *
 * @author mimic
 */
public class BotnetException extends BotnetCoreException {

    private final String resultCode;

    public BotnetException(String resultCode) {
        super();
        this.resultCode = resultCode;
    }

    public BotnetException(String resultCode, String message) {
        super(message);
        this.resultCode = resultCode;
    }

    public BotnetException(String resultCode, String message, Throwable cause) {
        super(message, cause);
        this.resultCode = resultCode;
    }

    public String getResultCode() {
        return resultCode;
    }
}
