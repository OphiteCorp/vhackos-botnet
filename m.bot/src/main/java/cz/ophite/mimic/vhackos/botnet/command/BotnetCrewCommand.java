package cz.ophite.mimic.vhackos.botnet.command;

import cz.ophite.mimic.vhackos.botnet.Botnet;
import cz.ophite.mimic.vhackos.botnet.api.module.CrewModule;
import cz.ophite.mimic.vhackos.botnet.api.net.response.CrewProfileResponse;
import cz.ophite.mimic.vhackos.botnet.command.base.BaseCommand;
import cz.ophite.mimic.vhackos.botnet.shared.command.Command;
import cz.ophite.mimic.vhackos.botnet.shared.command.CommandParam;
import cz.ophite.mimic.vhackos.botnet.shared.injection.Autowired;
import cz.ophite.mimic.vhackos.botnet.shared.injection.Inject;
import cz.ophite.mimic.vhackos.botnet.shared.utils.ascii.AsciiMaker;

import java.util.Map;

/**
 * Příkazy kolem crew.
 *
 * @author mimic
 */
@Inject
public class BotnetCrewCommand extends BaseCommand {

    @Autowired
    private CrewModule crewModule;

    protected BotnetCrewCommand(Botnet botnet) {
        super(botnet);
    }

    /**
     * Získá profil crew.
     */
    @Command(value = "crew profile", comment = "Gets a crew profile")
    private String getProfile(@CommandParam("crewId") long crewId) {
        return execute("crew profile -> " + crewId, am -> {
            am.setInsideTheme();

            var data = crewModule.getProfile(crewId);
            var fields = getFields(data, true);
            addCrewProfileResponseToAsciiMaker(am, fields);
        });
    }

    // === Pomocné metody
    // ================================================================================================================

    private void addCrewProfileResponseToAsciiMaker(AsciiMaker am, Map<String, BotnetCommands.FieldData> fields) {
        var logo = fields.remove(CrewProfileResponse.P_CREW_LOGO);
        put(am, logo);
        put(am, fields.remove(CrewProfileResponse.P_CREW_NAME));
        put(am, fields.remove(CrewProfileResponse.P_CREW_TAG));
        put(am, fields.remove(CrewProfileResponse.P_CREW_MEMBERS));
        put(am, fields.remove(CrewProfileResponse.P_CREW_REPUTATION));
        putRemainings(am, fields, logo != null);
    }
}
