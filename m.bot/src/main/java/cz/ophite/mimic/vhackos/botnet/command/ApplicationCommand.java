package cz.ophite.mimic.vhackos.botnet.command;

import cz.ophite.mimic.vhackos.botnet.Application;
import cz.ophite.mimic.vhackos.botnet.Botnet;
import cz.ophite.mimic.vhackos.botnet.Constants;
import cz.ophite.mimic.vhackos.botnet.api.IBotnet;
import cz.ophite.mimic.vhackos.botnet.command.base.BaseCommand;
import cz.ophite.mimic.vhackos.botnet.config.ApplicationConfig;
import cz.ophite.mimic.vhackos.botnet.config.ConfigHelper;
import cz.ophite.mimic.vhackos.botnet.config.ConfigProvider;
import cz.ophite.mimic.vhackos.botnet.shared.command.Command;
import cz.ophite.mimic.vhackos.botnet.shared.command.CommandParam;
import cz.ophite.mimic.vhackos.botnet.shared.command.CommandRunner;
import cz.ophite.mimic.vhackos.botnet.shared.injection.Autowired;
import cz.ophite.mimic.vhackos.botnet.shared.injection.Inject;
import cz.ophite.mimic.vhackos.botnet.shared.injection.InjectionContext;
import de.vandermeer.skb.interfaces.transformers.textformat.TextAlignment;

import java.lang.reflect.Parameter;

/**
 * Příkazy kolem aplikace.
 *
 * @author mimic
 */
@Inject
public final class ApplicationCommand extends BaseCommand {

    private static final String CMD_HELP = "help";
    private static final String CMD_HELP_COMMENT = "Displays the help you see now";

    private static final String CMD_ABOUT = "about";
    private static final String CMD_ABOUT_COMMENT = "Gets information about the current version";

    public static final String CMD_LATEST = "latest";
    private static final String CMD_LATEST_COMMENT = "Gets a list of changes in the latest version of Botnet";

    private static final String CMD_CONFIG = "config";
    private static final String CMD_CONFIG_COMMENT = "Get the current configuration";

    private static final String CMD_RELOAD = "reload";
    private static final String CMD_RELOAD_COMMENT = "Forces reloading configuration";

    private static final String CMD_LOGO = "logo";
    private static final String CMD_LOGO_COMMENT = "Prints the logo";

    public static final String CMD_EXIT = "q";
    private static final String CMD_EXIT_COMMENT = "Exit Botnet";

    @Autowired
    private ConfigProvider configProvider;

    @Autowired
    private ApplicationConfig config;

    protected ApplicationCommand(Botnet botnet) {
        super(botnet);
    }

    /**
     * Vypíše nápovědu.
     */
    @Command(value = CMD_HELP, hidden = true, comment = CMD_HELP_COMMENT)
    private String getHelp() {
        return execute("help - Available commands", am -> {
            put(am, "." + CMD_HELP, CMD_HELP_COMMENT);
            put(am, "." + CMD_LATEST, CMD_LATEST_COMMENT);
            put(am, "." + CMD_CONFIG, CMD_CONFIG_COMMENT);
            put(am, "." + CMD_RELOAD, CMD_RELOAD_COMMENT);
            put(am, "." + CMD_LOGO, CMD_LOGO_COMMENT);
            am.addRule();

            var runner = CommandRunner.getInstance();
            var commands = runner.getCommands();

            for (var entry : commands.entrySet()) {
                if (entry.getKey() == null) {
                    for (var cmd : entry.getValue().entrySet()) {
                        var command = cmd.getKey();
                        var method = runner.getCommandMethod(command);
                        var params = (method.getParameterCount() == 0) ? "" : " [" + paramsToString(method
                                .getParameters()) + "]";
                        var a = method.getAnnotation(Command.class);

                        if (!a.hidden()) {
                            var comment = a.comment().isEmpty() ? "" : a.comment();
                            am.add(String.format(".%s%s", command, params), comment);
                        }
                    }
                }
            }
            am.addRule();
            put(am, "." + CMD_ABOUT, CMD_ABOUT_COMMENT);
            put(am, "." + CMD_EXIT, CMD_EXIT_COMMENT);
            am.addRule();

            var copyRow = put(am, null, "by " + Constants.AUTHOR + " | v" + IBotnet.VERSION);
            copyRow.getCells().get(1).getContext().setTextAlignment(TextAlignment.RIGHT);
        });
    }

    /**
     * Vypíše logo.
     */
    @Command(value = CMD_LOGO, hidden = true, comment = CMD_LOGO_COMMENT)
    private String logo() {
        return execute("logo", am -> {
            am.setTopTheme();
            put(am, " ", Application.LOGO);
        });
    }

    /**
     * Přenačte konfiguraci aplikace.
     */
    @Command(value = CMD_RELOAD, hidden = true, comment = CMD_RELOAD_COMMENT)
    private String reloadConfig() {
        return execute("reload configuration", am -> {
            var config = configProvider.getAppConfig();
            InjectionContext.getInstance().get(ApplicationConfig.class).set(config);
            put(am, "Info", "The configuration has been reloaded");
        });
    }

    /**
     * Získá konfiguraci.
     */
    @Command(value = CMD_CONFIG, hidden = true, comment = CMD_CONFIG_COMMENT)
    private String getConfig() {
        return execute("configuration", am -> {
            var map = ConfigHelper.asMap(config);

            for (var entry : map.entrySet()) {
                put(am, entry.getKey(), entry.getValue());
            }
        });
    }

    /**
     * Získá seznam změn v poslední verzi Botnetu.
     */
    @Command(value = CMD_LATEST, hidden = true, comment = CMD_LATEST_COMMENT)
    private String getLatest() {
        return execute("latest", am -> {
            var data = getBotnet().getSharedData().getUpdateData();

            if (data == null) {
                put(am, "Error", "The service for checking the latest version is either turned off or there was an error getting information");
            } else {
                put(am, "Your Version", "v" + IBotnet.VERSION);
                am.addRule();
                put(am, "Latest Version", "v" + data.getVersion());
                put(am, "Download", data.getDownloadLink());
                put(am, "Discord", data.getDiscordLink());

                if (data.getNews() != null && !data.getNews().isEmpty()) {
                    for (var i = 0; i < data.getNews().size(); i++) {
                        var item = data.getNews().get(i);
                        put(am, (i == 0) ? "News" : "", "- " + item);
                    }
                } else {
                    put(am, "News", "No news found");
                }
                if (data.getNotice() != null && !data.getNotice().isEmpty()) {
                    am.addRule();
                    put(am, "Notice", data.getNotice());
                }
            }
        });
    }

    /**
     * Získá informace o aktuální verzi.
     */
    @Command(value = CMD_ABOUT, hidden = true, comment = CMD_ABOUT_COMMENT)
    private String getVersion() {
        return execute("about", am -> {
            put(am, "Version", "v" + IBotnet.VERSION);
            put(am, "Game API", getBotnet().getConfig().getGameApi());
            put(am, "Author", Constants.AUTHOR);
            put(am, "Year", Constants.YEAR);
            am.addRule();
            put(am, "Discord", Constants.DISCORD);
            put(am, "GitHub", Constants.REPOSITORY);
        });
    }

    // === Pomocné metody
    // ================================================================================================================

    private static String paramsToString(Parameter[] params) {
        var sb = new StringBuilder();
        int i = 0;

        for (var p : params) {
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
}
