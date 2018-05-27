package cz.ophite.mimic.vhackos.botnet.api.module;

import cz.ophite.mimic.vhackos.botnet.api.IBotnet;
import cz.ophite.mimic.vhackos.botnet.api.module.base.Module;
import cz.ophite.mimic.vhackos.botnet.api.module.base.ModuleHelper;
import cz.ophite.mimic.vhackos.botnet.api.net.response.MissionResponse;
import cz.ophite.mimic.vhackos.botnet.api.opcode.MissionsClaimDayOpcode;
import cz.ophite.mimic.vhackos.botnet.api.opcode.MissionsClaimOpcode;
import cz.ophite.mimic.vhackos.botnet.api.opcode.MissionsOpcode;
import cz.ophite.mimic.vhackos.botnet.api.opcode.base.Opcode;
import cz.ophite.mimic.vhackos.botnet.shared.injection.Inject;

import java.util.Collections;

/**
 * Správa misí.
 *
 * @author mimic
 */
@Inject
public final class MissionsModule extends Module {

    protected MissionsModule(IBotnet botnet) {
        super(botnet);
    }

    /**
     * Získá informace o misích.
     */
    public synchronized MissionResponse getMissions() {
        var opcode = new MissionsOpcode();
        return createMissionResponse(opcode);
    }

    /**
     * Sebere denní odměnu.
     */
    public synchronized MissionResponse claimDaily() {
        var opcode = new MissionsClaimDayOpcode();
        return createMissionResponse(opcode);
    }

    /**
     * Sebere odměnu.
     */
    public synchronized MissionResponse claimMissionReward(int dailyId) {
        var opcode = new MissionsClaimOpcode();
        opcode.setDailyId(dailyId);
        return createMissionResponse(opcode);
    }

    private MissionResponse createMissionResponse(Opcode opcode) {
        var response = sendRequest(opcode);
        var dto = new MissionResponse();
        ModuleHelper.checkResponseIntegrity(response.keySet(), MissionResponse.class);
        ModuleHelper.setField(response, dto, MissionResponse.P_STAGE);
        ModuleHelper.setField(response, dto, MissionResponse.P_CLAIM);
        ModuleHelper.setField(response, dto, MissionResponse.P_CLAIMED);
        ModuleHelper.setField(response, dto, MissionResponse.P_CLAIM_NEXT_DAY);
        ModuleHelper.setField(response, dto, MissionResponse.P_NEXT_DAILY_RESET);
        ModuleHelper.setField(response, dto, MissionResponse.P_DAILY_COUNT);
        ModuleHelper.setField(response, dto, MissionResponse.P_REWARD_BOOSTERS);
        ModuleHelper.setField(response, dto, MissionResponse.P_REWARD_NETCOINS);
        ModuleHelper.setField(response, dto, MissionResponse.P_REWARD_EXPERIENCE);

        if (!ModuleHelper.setField(response, dto, MissionResponse.P_DAILY, (f, data) -> ModuleHelper
                .convertToMissionItemData(data))) {
            dto.setDaily(Collections.emptyList());
        }
        return dto;
    }
}
