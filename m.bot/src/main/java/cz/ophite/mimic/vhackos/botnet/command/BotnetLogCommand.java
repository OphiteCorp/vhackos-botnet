package cz.ophite.mimic.vhackos.botnet.command;

import cz.ophite.mimic.vhackos.botnet.Botnet;
import cz.ophite.mimic.vhackos.botnet.api.module.LogModule;
import cz.ophite.mimic.vhackos.botnet.command.base.BaseCommand;
import cz.ophite.mimic.vhackos.botnet.db.service.DatabaseService;
import cz.ophite.mimic.vhackos.botnet.shared.command.Command;
import cz.ophite.mimic.vhackos.botnet.shared.command.CommandParam;
import cz.ophite.mimic.vhackos.botnet.shared.injection.Autowired;
import cz.ophite.mimic.vhackos.botnet.shared.injection.Inject;
import org.apache.commons.lang3.StringUtils;

/**
 * Příkazy kolem logů.
 *
 * @author mimic
 */
@Inject
public final class BotnetLogCommand extends BaseCommand {

    @Autowired
    private DatabaseService databaseService;

    @Autowired
    private LogModule logModule;

    protected BotnetLogCommand(Botnet botnet) {
        super(botnet);
    }

    /**
     * Získá vlastní systémový log.
     */
    @Command(value = "log", comment = "Gets own system log")
    private String getSystemLog() {
        return execute("log", am -> {
            var data = logModule.getLog();

            for (var i = 0; i < data.size(); i++) {
                put(am, (i == 0) ? "Log" : "", data.get(i));
            }
        });
    }

    /**
     * Nastaví vlastní systémový log.
     */
    @Command(value = "log set", comment = "Sets own system log")
    private String setSystemLog(@CommandParam("message") String message) {
        return execute("log set", am -> {
            logModule.setLog(message);
            put(am, "Result", "The message to the log has been set");
        });
    }

    /**
     * Získá informace o vzdáleném logu.
     */
    @Command(value = "log remote", comment = "Get the log from the target IP")
    private String getRemoteSystemLog(@CommandParam("ip") String ip) {
        return execute("remote log -> " + ip, am -> {
            var data = logModule.getRemoteLog(ip);

            for (var i = 0; i < data.size(); i++) {
                put(am, (i == 0) ? "Log" : "", data.get(i));
            }
            getLog().info("Adding the log to the database for IP: {}", ip);
            databaseService.addLog(ip, StringUtils.join(data, '\n'));
        });
    }

    /**
     * Nastaví vzdálený log.
     */
    @Command(value = "log remote set", comment = "Set the log from the target IP")
    private String setRemoteSystemLog(@CommandParam("ip") String ip, @CommandParam("message") String message) {
        return execute("remote log set -> " + ip, am -> {
            logModule.setRemoteLog(ip, message);
            put(am, "Result", "The message to the log has been set");
        });
    }
}
