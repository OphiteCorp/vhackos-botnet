package cz.ophite.mimic.vhackos.botnet.command;

import cz.ophite.mimic.vhackos.botnet.Botnet;
import cz.ophite.mimic.vhackos.botnet.api.module.CrewModule;
import cz.ophite.mimic.vhackos.botnet.api.net.response.CrewProfileResponse;
import cz.ophite.mimic.vhackos.botnet.api.net.response.CrewResponse;
import cz.ophite.mimic.vhackos.botnet.api.net.response.data.CrewMemberData;
import cz.ophite.mimic.vhackos.botnet.api.net.response.data.CrewMessageData;
import cz.ophite.mimic.vhackos.botnet.command.base.BaseCommand;
import cz.ophite.mimic.vhackos.botnet.shared.ascii.AsciiMaker;
import cz.ophite.mimic.vhackos.botnet.shared.command.Command;
import cz.ophite.mimic.vhackos.botnet.shared.command.CommandParam;
import cz.ophite.mimic.vhackos.botnet.shared.dto.CrewPositionType;
import cz.ophite.mimic.vhackos.botnet.shared.injection.Autowired;
import cz.ophite.mimic.vhackos.botnet.shared.injection.Inject;
import cz.ophite.mimic.vhackos.botnet.shared.utils.SharedUtils;
import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

/**
 * Příkazy kolem crew.
 *
 * @author mimic
 */
@Inject
public class BotnetCrewCommand extends BaseCommand {

    private static final SimpleDateFormat MESSAGE_TIME_FORMAT;

    static {
        MESSAGE_TIME_FORMAT = new SimpleDateFormat("dd.MM.yyyy | HH:mm:ss");
        MESSAGE_TIME_FORMAT.setTimeZone(TimeZone.getTimeZone("GMT+1"));
    }

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

    /**
     * Získá informace o aktuální crew.
     */
    @Command(value = "crew", comment = "Gets information on the current crew")
    private String getCrew() {
        return execute("crew", am -> {
            am.setInsideTheme();

            var data = crewModule.getCrew();
            var fields = getFields(data, true);
            addCrewResponseToAsciiMaker(am, fields);
        });
    }

    /**
     * Odešle crew zprávu.
     */
    @Command(value = "crew msg", comment = "Sends crew a message")
    private String sendMessage(@CommandParam("message") String message) {
        return execute("send crew message", am -> {
            am.setInsideTheme();

            var data = crewModule.sendMessage(message);
            var fields = getFields(data, true);
            addCrewResponseToAsciiMaker(am, fields);
        });
    }

    /**
     * Opustí crew.
     */
    @Command(value = "crew leave", comment = "Leaves the crew")
    private String leaveCrew() {
        return execute("leave crew", am -> {
            am.setInsideTheme();

            var data = crewModule.leaveCrew();
            var fields = getFields(data, true);
            addCrewResponseToAsciiMaker(am, fields);
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

    private void addCrewResponseToAsciiMaker(AsciiMaker am, Map<String, BotnetCommands.FieldData> fields) {
        var logo = fields.remove(CrewResponse.P_CREW_LOGO);
        put(am, logo);
        put(am, fields.remove(CrewResponse.P_CREW_NAME));
        put(am, fields.remove(CrewResponse.P_CREW_TAG));
        put(am, fields.remove(CrewResponse.P_CREW_MEMBERS));
        put(am, fields.remove(CrewResponse.P_CREW_REPUTATION));
        put(am, fields.remove(CrewResponse.P_CREW_RANK));
        put(am, fields.remove(CrewResponse.P_CREW_OWNER));
        put(am, fields.remove(CrewResponse.P_CREW_MEMBERS));
        put(am, fields.remove(CrewResponse.P_CREW_ID));
        put(am, fields.remove(CrewResponse.P_USER_ID));
        put(am, fields.remove(CrewResponse.P_USER_NAME));
        put(am, fields.remove(CrewResponse.P_MESSAGE));

        Object time = fields.remove(CrewResponse.P_TIME);
        if (time != null) {
            time = MESSAGE_TIME_FORMAT.format(calculateMessageTime((Long) ((FieldData) time).rawValue));
        }
        put(am, "Time", time);

        var membersLb = fields.remove(CrewResponse.P_MEMBERS);
        var members = (List<CrewMemberData>) membersLb.value;
        var messagesLb = fields.remove(CrewResponse.P_MESSAGES);
        var messages = (List<CrewMessageData>) messagesLb.value;

        putRemainings(am, fields, logo != null);

        if (!members.isEmpty()) {
            am.addRule();
            convertCrewMemberData(am, membersLb.name, members);
        }
        if (!messages.isEmpty()) {
            am.addRule();
            convertCrewMessageData(am, messagesLb.name, messages);
        }
    }

    private void convertCrewMemberData(AsciiMaker am, String name, List<CrewMemberData> data) {
        for (var i = 0; i < data.size(); i++) {
            var lb = data.get(i);
            var lastOnline = SharedUtils.toTimeFormat(lb.getLastOnline() * 1000);
            var position = CrewPositionType.getbyPosition(lb.getPosition());
            var str = String.format("%s | %s %s %s %s", StringUtils.leftPad(position.getAlias(), 10), StringUtils
                    .rightPad(lb.getUserId().toString(), 7), StringUtils.rightPad(lb.getUserName(), 20), StringUtils
                    .rightPad(lb.getLevel().toString(), 2), StringUtils.leftPad(lastOnline, 15));

            put(am, (i == 0) ? name : "", str);
        }
    }

    private void convertCrewMessageData(AsciiMaker am, String name, List<CrewMessageData> data) {
        for (var i = data.size() - 1; i >= 0; i--) {
            var lb = data.get(i);
            var time = MESSAGE_TIME_FORMAT.format(calculateMessageTime(lb.getTime()));
            var str = String.format("%s | %s %s", StringUtils.leftPad(lb.getUserId().toString(), 7), StringUtils
                    .rightPad(lb.getUserName(), 20), StringUtils.rightPad(time, 17));

            put(am, (i == data.size() - 1) ? name : "", str);
            put(am, "", lb.getMessage());
            put(am, "", "");
        }
    }

    private static Date calculateMessageTime(Long time) {
        return new Date(System.currentTimeMillis() - (((System.currentTimeMillis() / 1000) - time) * 1000));
    }
}
