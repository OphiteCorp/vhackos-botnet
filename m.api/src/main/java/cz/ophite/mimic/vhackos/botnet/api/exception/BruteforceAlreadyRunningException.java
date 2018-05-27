package cz.ophite.mimic.vhackos.botnet.api.exception;

/**
 * Bruteforce banky již běží.
 *
 * @author mimic
 */
public final class BruteforceAlreadyRunningException extends BotnetException {

    public BruteforceAlreadyRunningException(String resultCode, String message) {
        super(resultCode, message);
    }
}
