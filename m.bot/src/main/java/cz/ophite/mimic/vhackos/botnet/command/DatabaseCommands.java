package cz.ophite.mimic.vhackos.botnet.command;

import cz.ophite.mimic.vhackos.botnet.command.base.BaseCommand;
import cz.ophite.mimic.vhackos.botnet.db.service.DatabaseService;
import cz.ophite.mimic.vhackos.botnet.shared.command.Command;
import cz.ophite.mimic.vhackos.botnet.shared.command.CommandParam;
import cz.ophite.mimic.vhackos.botnet.shared.injection.Autowired;
import cz.ophite.mimic.vhackos.botnet.shared.injection.Inject;
import org.apache.commons.lang3.StringUtils;

/**
 * Databázové příkazy.
 *
 * @author mimic
 */
@Inject
public final class DatabaseCommands extends BaseCommand {

    @Autowired
    private DatabaseService databaseService;

    /**
     * Vyhledá v DB všechny uživatele, kteří mají tuto IP.
     */
    @Command(value = "db find ip", comment = "Search for IP from user list")
    private String dbSearchUserIp(@CommandParam("ip") String ip) {
        return execute("DB | search user -> " + ip, am -> {
            var data = databaseService.searchIp(ip);

            if (!data.isEmpty()) {
                for (var i = 0; i < data.size(); i++) {
                    var user = data.get(i);
                    var fields = getFields(user, true);

                    for (var entry : fields.entrySet()) {
                        put(am, entry);
                    }
                    if (i < data.size() - 1) {
                        am.add("", StringUtils.repeat('-', 20));
                    }
                }
            } else {
                am.add("Result", "This IP address was not found");
            }
        });
    }

    /**
     * Vyhledá všechny IP pro ID uživatele.
     */
    @Command(value = "db find uid", comment = "Search IP by user ID")
    private String dbSearchIpByUserId(@CommandParam("uid") int userId) {
        return execute("DB | search user by ID -> " + userId, am -> {
            var data = databaseService.searchIpByUserId(userId);

            if (!data.isEmpty()) {
                for (var i = 0; i < data.size(); i++) {
                    var ip = data.get(i);
                    put(am, (i == 0) ? "IP" : "", ip);
                }
            } else {
                put(am, "Result", "IP for user ID was not found");
            }
        });
    }
}
