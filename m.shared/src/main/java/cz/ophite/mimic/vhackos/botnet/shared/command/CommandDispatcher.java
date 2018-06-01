package cz.ophite.mimic.vhackos.botnet.shared.command;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/**
 * Pomocná třída pro čtení příkazů z konzole v samostatným vlákně.
 *
 * @author mimic
 */
public final class CommandDispatcher {

    private static final Logger LOG = LoggerFactory.getLogger(CommandDispatcher.class);

    static final String COMMANDS_PREFIX = ".";

    private static final CommandDispatcher INSTANCE = new CommandDispatcher();

    private final ExecutorService executor;
    private final List<ICommandListener> listeners = new ArrayList<>();
    private volatile boolean running = true;

    private CommandDispatcher() {
        executor = Executors.newFixedThreadPool(1, new CommandThreadFactory());
        executor.submit(new Operator());
    }

    /**
     * Získá instanci dispatcheru.
     */
    public static CommandDispatcher getInstance() {
        return INSTANCE;
    }

    /**
     * Přidá listener pro naslouchání příkazů.
     */
    public void addListener(ICommandListener listener) {
        listeners.add(listener);
    }

    /**
     * Zastaví čtení příkazů z konzole.
     */
    public synchronized void shutdown() {
        if (running) {
            running = false;
            executor.shutdown();
        }
    }

    /**
     * Zavolá příkaz manuálně.
     */
    public void call(String command) {
        if (StringUtils.isNotEmpty(command) && command.startsWith(COMMANDS_PREFIX)) {
            command = command.substring(COMMANDS_PREFIX.length());

            if (command.length() > 0) {
                for (var listener : listeners) {
                    try {
                        listener.incomingCommand(CommandDispatcher.INSTANCE, command);
                    } catch (Exception e) {
                        LOG.error("An unexpected error occurred. Make sure you have treated all the exceptions", e);
                    }
                }
            }
        }
    }

    private final class Operator implements Runnable {

        @Override
        public void run() {
            try (var br = new BufferedReader(new InputStreamReader(System.in))) {
                while (running) {
                    var line = br.readLine();
                    call(line);
                }
            } catch (IOException e) {
                // nic
            }
        }
    }

    private static final class CommandThreadFactory implements ThreadFactory {

        @Override
        public Thread newThread(Runnable r) {
            var t = new Thread(r, getClass().getSimpleName());
            t.setPriority(Thread.MIN_PRIORITY);
            t.setDaemon(false);
            return t;
        }
    }
}
