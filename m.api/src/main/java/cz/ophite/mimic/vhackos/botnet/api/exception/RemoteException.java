package cz.ophite.mimic.vhackos.botnet.api.exception;

/**
 * Nastala chyba při získání nějakých informací ze vzdálené IP.
 *
 * @author mimic
 */
public final class RemoteException extends BotnetException {

    public RemoteException(String resultCode, String message) {
        super(resultCode, message);
    }
}
