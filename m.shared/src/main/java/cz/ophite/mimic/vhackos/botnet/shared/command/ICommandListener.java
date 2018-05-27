package cz.ophite.mimic.vhackos.botnet.shared.command;

/**
 * Listener, který se volá při každém odeslanám příkazu v konzoli.
 *
 * @author mimic
 */
public interface ICommandListener {

    /**
     * Příchozí příkaz z konzole.
     */
    void incomingCommand(CommandDispatcher dispatcher, String command) throws Exception;
}
