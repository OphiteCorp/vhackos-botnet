package cz.ophite.mimic.vhackos.botnet.command;

import cz.ophite.mimic.vhackos.botnet.Botnet;
import cz.ophite.mimic.vhackos.botnet.api.module.MissionsModule;
import cz.ophite.mimic.vhackos.botnet.api.net.response.MissionResponse;
import cz.ophite.mimic.vhackos.botnet.api.net.response.data.MissionItemData;
import cz.ophite.mimic.vhackos.botnet.command.base.BaseCommand;
import cz.ophite.mimic.vhackos.botnet.shared.ascii.AsciiMaker;
import cz.ophite.mimic.vhackos.botnet.shared.command.Command;
import cz.ophite.mimic.vhackos.botnet.shared.command.CommandParam;
import cz.ophite.mimic.vhackos.botnet.shared.dto.MissionFinishedType;
import cz.ophite.mimic.vhackos.botnet.shared.injection.Autowired;
import cz.ophite.mimic.vhackos.botnet.shared.injection.Inject;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * Příkazy pro správu misí.
 *
 * @author mimic
 */
@Inject
public final class BotnetMissionsCommand extends BaseCommand {

    @Autowired
    private MissionsModule missionsModule;

    protected BotnetMissionsCommand(Botnet botnet) {
        super(botnet);
    }

    /**
     * Zobrazí informace o mineru.
     */
    @Command(value = "missions", comment = "Gets information about missions")
    private String getMissions() {
        return execute("missions", am -> {
            var data = missionsModule.getMissions();
            var fields = getFields(data, true);
            addMissionsResponseToAsciiMaker(am, fields);
        });
    }

    /**
     * Sebere denní odměnu.
     */
    @Command(value = "mission claim day", comment = "Takes a daily reward")
    private String claimDayReward() {
        return execute("mission claim day reward", am -> {
            var data = missionsModule.claimDaily();
            var fields = getFields(data, true);
            addMissionsResponseToAsciiMaker(am, fields);
        });
    }

    /**
     * Sebere denní odměnu.
     */
    @Command(value = "mission claim", comment = "Complete the mission")
    private String claimMissionReward(@CommandParam("dailyId") int dailyId) {
        return execute("mission claim -> " + dailyId, am -> {
            var data = missionsModule.claimMissionReward(dailyId);
            var fields = getFields(data, true);
            addMissionsResponseToAsciiMaker(am, fields);
        });
    }

    // === Pomocné metody
    // ================================================================================================================

    private void addMissionsResponseToAsciiMaker(AsciiMaker am, Map<String, FieldData> fields) {
        put(am, fields.remove(MissionResponse.P_NEXT_DAILY_RESET));
        put(am, fields.remove(MissionResponse.P_CLAIM_NEXT_DAY));
        put(am, fields.remove(MissionResponse.P_DAILY_COUNT));
        convertMissions(am, fields.remove(MissionResponse.P_DAILY));
        putRemainings(am, fields);
    }

    private void convertMissions(AsciiMaker am, FieldData data) {
        var missions = (List<MissionItemData>) data.value;
        var name = data.name;

        for (var i = 0; i < missions.size(); i++) {
            var mission = missions.get(i);
            var finished = MissionFinishedType.getByCode(mission.getFinished()).getAlias();

            var str = String
                    .format("ID %s | %s -> %s | %s xp | %s", StringUtils.leftPad(String.valueOf(i), 1), StringUtils
                            .rightPad(mission.getRewardType(), 8), StringUtils
                            .leftPad(mission.getRewardAmount().toString(), 4), StringUtils
                            .leftPad(mission.getExperience().toString(), 4), StringUtils.leftPad(finished, 8));

            put(am, (i == 0) ? name : "", str);
        }
        if (missions.isEmpty()) {
            put(am, name, "<none>");
        }
    }
}
