package cz.ophite.mimic.vhackos.botnet.shared.command;

/**
 * Příkaz vyžaduje parametry které mu nebyly poskytnuty.
 *
 * @author mimic
 */
public final class CommandInvalidParamsException extends RuntimeException {

    CommandInvalidParamsException(String message) {
        super(message);
    }
}
