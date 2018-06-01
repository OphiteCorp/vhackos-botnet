package cz.ophite.mimic.vhackos.botnet;

import cz.ophite.mimic.vhackos.botnet.api.IBotnet;
import cz.ophite.mimic.vhackos.botnet.api.exception.AccountBlockedException;
import cz.ophite.mimic.vhackos.botnet.api.exception.ConnectionException;
import cz.ophite.mimic.vhackos.botnet.api.exception.InvalidLoginException;
import cz.ophite.mimic.vhackos.botnet.config.ApplicationConfig;
import cz.ophite.mimic.vhackos.botnet.config.ConfigProvider;
import cz.ophite.mimic.vhackos.botnet.db.HibernateManager;
import cz.ophite.mimic.vhackos.botnet.db.exception.DatabaseConnectionException;
import cz.ophite.mimic.vhackos.botnet.exception.MissingLoginCredentialException;
import cz.ophite.mimic.vhackos.botnet.gui.BotnetGui;
import cz.ophite.mimic.vhackos.botnet.service.base.EndpointService;
import cz.ophite.mimic.vhackos.botnet.service.base.Service;
import cz.ophite.mimic.vhackos.botnet.shared.command.*;
import cz.ophite.mimic.vhackos.botnet.shared.injection.IInjectRule;
import cz.ophite.mimic.vhackos.botnet.shared.injection.InjectionContext;
import cz.ophite.mimic.vhackos.botnet.shared.utils.ascii.AsciiMaker;
import de.vandermeer.skb.interfaces.transformers.textformat.TextAlignment;
import org.apache.commons.exec.CommandLine;
import org.reflections.Reflections;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Parameter;
import java.sql.SQLException;
import java.util.*;

/**
 * Hlavní třída aplikace.
 *
 * @author mimic
 */
public final class Application implements ICommandListener {

    private static final Logger LOG = LoggerFactory.getLogger(Application.class);

    public static final int ERROR_CODE_MISSING_LOGIN_CREDENTIAL = 10;
    public static final String LOGO = getBotnetLogo();

    private static Botnet botnet;
    private static BotnetGui gui;

    /**
     * EntryPoint aplikace.
     */
    public static void main(String[] args) {
        var time = System.currentTimeMillis();
        var useGui = !(args != null && args.length > 0 && "-console".equalsIgnoreCase(args[0]));

        if (useGui) {
            gui = new BotnetGui();
            gui.open();
        }
        LOG.info(LOGO);
        LOG.info("The botnet starting...");

        // vytvoří instanci aplikace a botnetu
        final var app = new Application();
        botnet = new Botnet();

        // vytvoří dependency injection
        var injectRules = prepareInjectRules();
        InjectionContext.initialize(new String[]{ "cz.ophite.mimic.vhackos.botnet" }, injectRules);
        InjectionContext.lazyInit(botnet);
        var context = InjectionContext.getInstance();

        // načte konfiguraci
        var config = context.get(ConfigProvider.class).getAppConfig();
        context.get(ApplicationConfig.class).set(config);

        // do načte všechny třídy, které jsou označené jako lazy (vyžadují např. konfiguraci aplikace)
        InjectionContext.initLazyClasses();

        if (gui != null) {
            gui.postProcessing(config);
        }
        // vytvoří databázové připojení
        if (config.isDbEnable()) {
            try {
                HibernateManager.initialize(config);
            } catch (DatabaseConnectionException e) {
                if (e.getCause() != null && e.getCause() instanceof SQLException) {
                    var eSql = (SQLException) e.getCause();
                    LOG.error("A database error has occurred: SQL state {}, message: {}", eSql.getSQLState(), eSql
                            .getMessage());
                } else {
                    LOG.error("An unexpected error occurred when connecting to the database. The database will not be used. Message: {}", e
                            .getMessage());
                }
            }
        }

        // vyhledá všechny dostupné příkazy
        CommandRunner.initialize(context);
        CommandRunner.initCommands(botnet);

        // zahájí čtení konzole pro příkazy
        var dispatcher = CommandDispatcher.getInstance();
        dispatcher.addListener(app);

        try {
            // spustí botnet
            botnet.start();

            time = System.currentTimeMillis() - time;
            LOG.info("The botnet was started in: {}ms", time);
            LOG.info("Type \".help\" to view available commands");

        } catch (MissingLoginCredentialException e) {
            LOG.error("The username or password is not filled in. Open the configuration file and edit it");
            exit(ERROR_CODE_MISSING_LOGIN_CREDENTIAL);

        } catch (AccountBlockedException e) {
            LOG.error("Your game account has been blocked. Try it later");
            exit(1);

        } catch (InvalidLoginException e) {
            LOG.error("Your username or password is incorrect");
            exit(1);

        } catch (ConnectionException e) {
            LOG.error("Unable to establish server connection. Check your login and password or proxy server settings or try it later");
            exit(1);

        } catch (Exception e) {
            LOG.error("An unexpected error occurred while booting the botnet", e);
            exit(1);
        }
    }

    /**
     * Ukončí aplikaci.
     */
    private static void exit(int exitCode) {
        LOG.info("Shutdown is in progress...");
        botnet.shutdown();
        HibernateManager.shutdown();
        LOG.info("Botnet stopped");

        if (gui != null) {
            gui.close(exitCode);
        } else {
            System.exit(exitCode);
        }
    }

    @Override
    public void incomingCommand(CommandDispatcher dispatcher, String command) {
        // vypíše nápovědu
        if (command.equalsIgnoreCase("help")) {
            printHelp();

            // ukončí aplikaci
        } else if (command.equalsIgnoreCase("q")) {
            dispatcher.shutdown();
            exit(0);

        } else {
            var runner = CommandRunner.getInstance();
            // vezme pouze příkazy bez kategorie + je potřeba seřadit příkazy dle jejich délky od (max->min)
            List<String> availableCommands = new ArrayList(runner.getCommands().get(null).keySet());
            availableCommands.sort((o1, o2) -> Integer.compare(o2.length(), o1.length()));
            String exec = null;

            for (var cmd : availableCommands) {
                if (command.startsWith(cmd)) {
                    // je potřeba odstranit příkaz, aby zbyly pouze parametry (pokud nějaké jsou), protože díky tomu
                    // je možné posílat "exec" příkaz složený i z mezer
                    command = command.substring(cmd.length()).trim();
                    exec = cmd;
                    break;
                }
            }
            var method = runner.getCommandMethod(exec);
            if (method != null) {
                // pokud příkaz obsahuje povinné parametry, tak jako exec nastavíme cokoliv jen proto, aby to
                // CommandLine rozparsoval jako exec a ponechal parametry v původním stavu - původní exec byl
                // odstraněn, protože chceme podporovat exec obsahující mezeru, což následný parser neumí - proto fake
                command = (method.getParameterCount() > 0) ? "fake_exec " + command : exec;
                var cmdLine = CommandLine.parse(command);
                var args = cmdLine.getArguments();

                // pokud se jedná o text složený z více slov, tak vždy je umístěn v uvozovkách a parser je ponechává,
                // takže je dodatečně odeberem
                if (args != null && args.length > 0) {
                    for (var i = 0; i < args.length; i++) {
                        var s = args[i];
                        if (s.equals("<null>")) {
                            args[i] = null;
                        } else if (s.startsWith("\"") && s.endsWith("\"")) {
                            args[i] = s.substring(1, s.length() - 1);
                        }
                    }
                }
                try {
                    var result = runner.run(exec, args);
                    printCommandResult(result);
                } catch (CommandInvalidParamsException e) {
                    // nic
                }
            } else {
                LOG.warn("The command '{}' does not exist", command);
            }
        }
    }

    private static Map<Class, IInjectRule> prepareInjectRules() {
        var rules = new HashMap<Class, IInjectRule>();
        rules.put(null, c -> InjectionContext.getConstructor(c, IBotnet.class).newInstance(botnet));

        var ref = new Reflections(Service.SERVICES_PACKAGE, new TypeAnnotationsScanner());
        var classes = ref.getTypesAnnotatedWith(EndpointService.class, true);

        for (var clazz : classes) {
            rules.put(clazz, c -> InjectionContext.getConstructor(c, Botnet.class).newInstance(botnet));
        }
        return rules;
    }

    private static void printHelp() {
        var runner = CommandRunner.getInstance();
        var commands = runner.getCommands();
        var am = new AsciiMaker();
        am.addRule();
        am.add(null, "Available commands");
        am.addRule();
        am.add(".help", "Displays the help you see now");

        for (var entry : commands.entrySet()) {
            if (entry.getKey() == null) {
                for (var cmd : entry.getValue().entrySet()) {
                    var command = cmd.getKey();
                    var method = runner.getCommandMethod(command);
                    var params = (method.getParameterCount() == 0) ? "" : " [" + paramsToString(method
                            .getParameters()) + "]";
                    var a = method.getAnnotation(Command.class);
                    var comment = a.comment().isEmpty() ? "" : a.comment();

                    am.add(String.format(".%s%s", command, params), comment);
                }
            }
        }
        am.add(".q", "Exit botnet");
        am.addRule();
        var copyRow = am.add(null, "by mimic | v" + IBotnet.VERSION);
        copyRow.getCells().get(1).getContext().setTextAlignment(TextAlignment.RIGHT);
        am.addRule();
        LOG.info("\n" + am.render() + "\n");
    }

    private static void printCommandResult(Object result) {
        // příkaz je typu void a nevrací žádnou hodnotu
        if (result != null && result instanceof Optional) {
            LOG.info("Executed!");
        } else {
            if (result != null) {
                LOG.info("\n" + result.toString() + "\n");
            } else {
                LOG.info("Returns: <NULL>");
            }
        }
    }

    private static String paramsToString(Parameter[] params) {
        var sb = new StringBuilder();
        int i = 0;

        for (Parameter p : params) {
            var name = p.getType().getSimpleName();

            if (p.isAnnotationPresent(CommandParam.class)) {
                var a = p.getDeclaredAnnotation(CommandParam.class);
                name = a.value();
            }
            sb.append(name);
            if (i++ < params.length - 1) {
                sb.append(", ");
            }
        }
        return sb.toString();
    }

    private static String getBotnetLogo() {
        char code[] = {
                '\n', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '_', '_', ' ', ' ', '_',
                '_', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '_', '_', ' ', ' ', ' ', ' ',
                ' ', ' ', '_', '_', '_', '_', ' ', ' ', '_', '_', '_', '_', '_', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ',
                ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ',
                '\n', ' ', ' ', ' ', ' ', ' ', ' ', '_', ' ', ' ', ' ', '_', '_', ' ', '/', ' ', '/', ' ', '/', ' ',
                '/', '_', '_', '_', '_', ' ', '_', ' ', '_', '_', '_', '_', '_', ' ', '/', ' ', '/', '_', '_', ' ', ' ',
                ' ', '/', ' ', '_', '_', ' ', '\\', '/', ' ', '_', '_', '_', '/', ' ', ' ', ' ', ' ', ' ', ' ', ' ',
                ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ',
                ' ', '\n', ' ', ' ', ' ', ' ', ' ', '|', ' ', '|', ' ', '/', ' ', '/', '/', ' ', '/', '_', '/', ' ',
                '/', '/', ' ', '_', '_', ' ', '`', '/', '/', ' ', '_', '_', '_', '/', '/', ' ', '/', '/', '_', '/', ' ',
                ' ', '/', ' ', '/', ' ', '/', ' ', '/', '\\', '_', '_', ' ', '\\', ' ', ' ', ' ', ' ', ' ', ' ', ' ',
                ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ',
                ' ', ' ', '\n', ' ', ' ', ' ', ' ', ' ', '|', ' ', '|', '/', ' ', '/', '/', ' ', '_', '_', ' ', ' ',
                '/', '/', ' ', '/', '_', '/', ' ', '/', '/', ' ', '/', '_', '_', ' ', '/', ' ', ',', '<', ' ', ' ', ' ',
                ' ', '/', ' ', '/', '_', '/', ' ', '/', '_', '_', '_', '/', ' ', '/', ' ', ' ', ' ', ' ', ' ', ' ', ' ',
                ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ',
                ' ', ' ', '\n', ' ', ' ', ' ', ' ', ' ', '|', '_', '_', '_', '/', '/', '_', '/', ' ', '/', '_', '/',
                ' ', '\\', '_', '_', ',', '_', '/', ' ', '\\', '_', '_', '_', '/', '/', '_', '/', '|', '_', '|', ' ',
                ' ', ' ', '\\', '_', '_', '_', '_', '/', '/', '_', '_', '_', '_', '/', ' ', ' ', ' ', ' ', ' ', ' ',
                ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ',
                ' ', ' ', ' ', ' ', '\n', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '_', '_', '_', '_', ' ', ' ', ' ', ' ',
                ' ', ' ', ' ', ' ', ' ', ' ', '_', '_', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ',
                ' ', ' ', ' ', '_', '_', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ',
                ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ',
                ' ', ' ', ' ', ' ', '\n', ' ', ' ', ' ', ' ', ' ', ' ', '/', ' ', '_', '_', ' ', ')', ' ', '_', '_',
                '_', '_', ' ', ' ', ' ', '/', ' ', '/', '_', ' ', '_', '_', '_', '_', ' ', ' ', ' ', '_', '_', '_', ' ',
                ' ', ' ', '/', ' ', '/', '_', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ',
                ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ',
                ' ', ' ', ' ', ' ', '\n', ' ', ' ', ' ', ' ', ' ', '/', ' ', '_', '_', ' ', ' ', '|', '/', ' ', '_',
                '_', ' ', '\\', ' ', '/', ' ', '_', '_', '/', '/', ' ', '_', '_', ' ', '\\', ' ', '/', ' ', '_', ' ',
                '\\', ' ', '/', ' ', '_', '_', '/', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ',
                ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ',
                ' ', ' ', ' ', ' ', ' ', ' ', '\n', ' ', '_', ' ', ' ', '/', ' ', '/', '_', '/', ' ', '/', '/', ' ',
                '/', '_', '/', ' ', '/', '/', ' ', '/', '_', ' ', '/', ' ', '/', ' ', '/', ' ', '/', '/', ' ', ' ', '_',
                '_', '/', '/', ' ', '/', '_', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ',
                ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ',
                ' ', ' ', ' ', ' ', ' ', ' ', '\n', '(', '_', ')', '/', '_', '_', '_', '_', '_', '/', ' ', '\\', '_',
                '_', '_', '_', '/', ' ', '\\', '_', '_', '/', '/', '_', '/', ' ', '/', '_', '/', ' ', '\\', '_', '_',
                '_', '/', ' ', '\\', '_', '_', '/', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ',
                ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ',
                ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '\n', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '_', '_', ' ', ' ',
                ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '_', '_', '_', '_', ' ', ' ', ' ', ' ', ' ',
                ' ', ' ', ' ', ' ', ' ', '_', '_', ' ', ' ', ' ', ' ', ' ', '_', ' ', ' ', '_', '_', ' ', ' ', ' ', ' ',
                ' ', ' ', ' ', ' ', ' ', '_', '_', '_', '_', '_', '_', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ',
                ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '\n', ' ', ' ', ' ', ' ', ' ', ' ', '/', ' ', '/', '_', ' ',
                ' ', ' ', '_', '_', ' ', ' ', '_', '_', ' ', ' ', ' ', '/', ' ', '_', '_', ' ', '\\', ' ', '_', '_',
                '_', '_', ' ', ' ', ' ', '/', ' ', '/', '_', ' ', ' ', ' ', '(', '_', ')', '/', ' ', '/', '_', ' ', '_',
                '_', '_', ' ', ' ', ' ', '/', ' ', '_', '_', '_', '_', '/', '_', '_', '_', '_', ' ', ' ', ' ', '_', '_',
                '_', '_', '_', ' ', '_', '_', '_', '_', ' ', '\n', ' ', ' ', ' ', ' ', ' ', '/', ' ', '_', '_', ' ',
                '\\', ' ', '/', ' ', '/', ' ', '/', ' ', '/', ' ', ' ', '/', ' ', '/', ' ', '/', ' ', '/', '/', ' ',
                '_', '_', ' ', '\\', ' ', '/', ' ', '_', '_', ' ', '\\', ' ', '/', ' ', '/', '/', ' ', '_', '_', '/',
                '/', ' ', '_', ' ', '\\', ' ', '/', ' ', '/', ' ', ' ', ' ', ' ', '/', ' ', '_', '_', ' ', '\\', ' ',
                '/', ' ', '_', '_', '_', '/', '/', ' ', '_', '_', ' ', '\\', '\n', ' ', ' ', ' ', ' ', '/', ' ', '/',
                '_', '/', ' ', '/', '/', ' ', '/', '_', '/', ' ', '/', ' ', ' ', '/', ' ', '/', '_', '/', ' ', '/', '/',
                ' ', '/', '_', '/', ' ', '/', '/', ' ', '/', ' ', '/', ' ', '/', '/', ' ', '/', '/', ' ', '/', '_', ' ',
                '/', ' ', ' ', '_', '_', '/', '/', ' ', '/', '_', '_', '_', ' ', '/', ' ', '/', '_', '/', ' ', '/', '/',
                ' ', '/', ' ', ' ', ' ', '/', ' ', '/', '_', '/', ' ', '/', '\n', ' ', ' ', ' ', '/', '_', '.', '_',
                '_', '_', '/', ' ', '\\', '_', '_', ',', ' ', '/', ' ', ' ', ' ', '\\', '_', '_', '_', '_', '/', '/',
                ' ', '.', '_', '_', '_', '/', '/', '_', '/', ' ', '/', '_', '/', '/', '_', '/', ' ', '\\', '_', '_',
                '/', ' ', '\\', '_', '_', '_', '/', ' ', '\\', '_', '_', '_', '_', '/', ' ', '\\', '_', '_', '_', '_',
                '/', '/', '_', '/', ' ', ' ', ' ', '/', ' ', '.', '_', '_', '_', '/', ' ', '\n', ' ', ' ', ' ', ' ',
                ' ', ' ', ' ', ' ', ' ', ' ', '/', '_', '_', '_', '_', '/', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ',
                '/', '_', '/', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ',
                ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ',
                ' ', ' ', ' ', ' ', ' ', ' ', '/', '_', '/', ' ', ' ', ' ', ' ', ' ', ' ', '\n' };
        return new String(code);
    }
}