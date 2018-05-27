package cz.ophite.mimic.vhackos.botnet.db.exception;

/**
 * Nastala chyba při připojení k databázi.
 *
 * @author mimic
 */
public final class DatabaseConnectionException extends RuntimeException {

    public DatabaseConnectionException(String message) {
        super(message);
    }

    public DatabaseConnectionException(String message, Throwable cause) {
        super(message, cause);
    }
}
