package cz.ophite.mimic.vhackos.botnet.config;

/**
 * Nastala chyba při vytvoření nebo získání konfiguračního souboru aplikace.
 *
 * @author mimic
 */
final class ConfigurationException extends RuntimeException {

    ConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }
}
