package cz.ophite.mimic.vhackos.botnet.shared.command;

import cz.ophite.mimic.vhackos.botnet.shared.injection.Inject;
import cz.ophite.mimic.vhackos.botnet.shared.injection.InjectionContext;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.*;

/**
 * Vyhledá všechny dostupné příkazy a umožní jejich spuštění.
 *
 * @author mimic
 */
public final class CommandRunner {

    private static final Logger LOG = LoggerFactory.getLogger(CommandRunner.class);

    private static final CommandRunner INSTANCE = new CommandRunner();

    private static Map<String, Method> commands;
    private static Map<String, TreeMap<String, List<Type>>> listedCommands;
    private static InjectionContext context;
    private static Map<String, Pair<Method, Object>> instances;

    private CommandRunner() {
    }

    /**
     * Získá instanci runneru.
     */
    public static CommandRunner getInstance() {
        return INSTANCE;
    }

    /**
     * Vyhledá všechny příkazy v kontextu.
     */
    public static synchronized void initialize(InjectionContext ctx) {
        if (ctx == null) {
            throw new NullPointerException("Input context must not be null");
        }
        if (context != null) {
            LOG.warn("Runner has already been created");
            return;
        }
        context = ctx;
        commands = new HashMap<>();
        instances = new HashMap<>();
        listedCommands = new LinkedHashMap<>();

        var injectedClasses = ctx.getInjectedClasses();
        LOG.debug("Found {} injected classes", injectedClasses.size());

        for (var alias : injectedClasses) {
            var instance = ctx.get(alias);

            if (instance != null) {
                var methods = instance.getClass().getDeclaredMethods();

                for (var method : methods) {
                    if (method.isAnnotationPresent(Command.class)) {
                        var a = method.getAnnotation(Command.class);
                        var commandAlias = a.value().isEmpty() ? method.getName() : a.value();

                        if (!commands.containsKey(commandAlias)) {
                            var entry = Map.entry(commandAlias.toLowerCase(), method);
                            commands.put(entry.getKey(), entry.getValue());
                            addToListedCommandsBasic(entry);
                        } else {
                            LOG.error("Command under this '{}' alias already exists. The command will be skipped", commandAlias);
                        }
                    }
                }
            } else {
                throw new IllegalStateException("No class found under alias '" + alias + "'");
            }
        }
    }

    /**
     * Vyhledá všechny příkazy v konkrétní instanci třídy.
     */
    public static synchronized void initCommands(Object classInstance) {
        if (classInstance == null) {
            throw new NullPointerException("The class input parameter must not be null");
        }
        if (context == null) {
            throw new IllegalStateException("Runner was not initialized");
        }
        if (classInstance.getClass().isAnnotationPresent(Inject.class)) {
            throw new IllegalStateException("This class is marked an Inject annotation, and you can not find commands in this way");
        }
        var clazz = classInstance.getClass();
        var methods = clazz.getDeclaredMethods();

        for (var method : methods) {
            if (method.isAnnotationPresent(Command.class)) {
                var a = method.getAnnotation(Command.class);
                var commandAlias = a.value().isEmpty() ? method.getName() : a.value();

                if (commands.containsKey(commandAlias) || instances.containsKey(commandAlias)) {
                    LOG.error("Command under this '{}' alias already exists. The command will be skipped", commandAlias);
                    continue;
                }
                var entry = Map.entry(commandAlias.toLowerCase(), new MutablePair<>(method, classInstance));
                instances.put(entry.getKey(), entry.getValue());
                addToListedCommandsAdvanced(entry);
            }
        }
    }

    /**
     * Spustí příkaz.
     */
    public <T> T run(String command, Object[] params) {
        var method = getCommandMethod(command);

        if (method != null) {
            var instance = getInstanceOfMethod(method);

            if (instance != null) {
                try {
                    var requiredParams = method.getParameterTypes();
                    var realParams = new ArrayList<>();

                    if (requiredParams.length > 0) {
                        if (params == null || params.length < requiredParams.length) {
                            var sb = new StringBuilder();
                            for (var i = 0; i < requiredParams.length; i++) {
                                var p = method.getParameters()[i];
                                if (p.isAnnotationPresent(CommandParam.class)) {
                                    sb.append(p.getDeclaredAnnotation(CommandParam.class).value()).append("[")
                                            .append(requiredParams[i].getSimpleName()).append("]");
                                } else {
                                    sb.append(requiredParams[i].getSimpleName());
                                }
                                if (i < requiredParams.length - 1) {
                                    sb.append(" ");
                                }
                            }
                            LOG.error("Invalid number of parameters. {} parameters are expected. Command must be: {}{} {}", requiredParams.length, CommandDispatcher.COMMANDS_PREFIX, command, sb);
                            throw new CommandInvalidParamsException("Invalid number of parameters: " + command);
                        }
                        for (var i = 0; i < params.length; i++) {
                            var type = requiredParams[i];
                            var value = params[i];

                            if (value != null) {
                                if (type.equals(String.class)) {
                                    value = String.valueOf(value);
                                } else if (type.equals(Integer.class) || type.equals(int.class)) {
                                    value = Integer.valueOf(value.toString());
                                } else if (type.equals(Long.class) || type.equals(long.class)) {
                                    value = Long.valueOf(value.toString());
                                } else if (type.equals(Byte.class) || type.equals(byte.class)) {
                                    value = Byte.valueOf(value.toString());
                                } else if (type.equals(Boolean.class) || type.equals(boolean.class)) {
                                    value = Boolean.valueOf(value.toString());
                                } else if (type.equals(Double.class) || type.equals(double.class)) {
                                    value = Double.valueOf(value.toString());
                                } else if (type.equals(Float.class) || type.equals(float.class)) {
                                    value = Float.valueOf(value.toString());
                                }
                            }
                            realParams.add(value);
                        }
                    }
                    method.setAccessible(true);
                    LOG.debug("Executing command: {}", command);
                    var result = method.invoke(instance, realParams.toArray());
                    LOG.debug("Command '{}' finished", command);

                    if (method.getReturnType().equals(Void.TYPE)) {
                        return (T) Optional.empty();
                    } else {
                        return (T) result;
                    }
                } catch (Exception e) {
                    if (e instanceof CommandInvalidParamsException) {
                        throw (CommandInvalidParamsException) e;
                    } else if (e instanceof ArrayIndexOutOfBoundsException) {
                        LOG.error("Invalid command or number / type of parameters");
                        throw new CommandInvalidParamsException("Invalid command or number / type of parameters");
                    } else {
                        LOG.error("An error occurred while running the command: " + command, e);
                    }
                }
            } else {
                throw new IllegalStateException("Could not get '" + method.getDeclaringClass() + "' class instance");
            }
        } else {
            LOG.warn("The command under the '{}' alias does not exist", command);
        }
        return null;
    }

    /**
     * Získá všechny dostupné příkazy.
     */
    public Map<String, TreeMap<String, List<Type>>> getCommands() {
        if (context == null) {
            throw new IllegalStateException("Runner was not initialized");
        }
        return listedCommands;
    }

    /**
     * Získá metodu příkazu.
     */
    public Method getCommandMethod(String command) {
        if (command == null) {
            return null;
        }
        if (context == null) {
            throw new IllegalStateException("Runner was not initialized");
        }
        var alias = command.toLowerCase().trim();
        var method = commands.getOrDefault(alias, null);

        if (method == null) {
            method = instances.containsKey(alias) ? instances.get(alias).getKey() : null;
        }
        return method;
    }

    private static void addToListedCommandsBasic(Map.Entry<String, Method> entry) {
        var a = entry.getValue().getAnnotation(Command.class);
        var category = a.category().isEmpty() ? null : a.category().toLowerCase();
        var data = listedCommands.computeIfAbsent(category, k -> new TreeMap<>());

        if (!data.containsKey(entry.getKey())) {
            var params = entry.getValue().getParameterTypes();
            data.put(entry.getKey(), Arrays.asList(params));
        }
    }

    private static void addToListedCommandsAdvanced(Map.Entry<String, MutablePair<Method, Object>> entry) {
        var a = entry.getValue().getKey().getAnnotation(Command.class);
        var category = a.category().isEmpty() ? null : a.category().toLowerCase();
        var data = listedCommands.computeIfAbsent(category, k -> new TreeMap<>());

        if (!data.containsKey(entry.getKey())) {
            var params = entry.getValue().getKey().getParameterTypes();
            data.put(entry.getKey(), Arrays.asList(params));
        }
    }

    private static Object getInstanceOfMethod(Method method) {
        var a = method.getAnnotation(Command.class);
        var commandAlias = a.value().isEmpty() ? method.getName() : a.value();

        if (instances.containsKey(commandAlias)) {
            return instances.get(commandAlias).getValue();
        }
        return context.get(method.getDeclaringClass());
    }
}
