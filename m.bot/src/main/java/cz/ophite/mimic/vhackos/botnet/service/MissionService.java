package cz.ophite.mimic.vhackos.botnet.service;

import cz.ophite.mimic.vhackos.botnet.Botnet;
import cz.ophite.mimic.vhackos.botnet.api.module.MissionsModule;
import cz.ophite.mimic.vhackos.botnet.service.base.EndpointService;
import cz.ophite.mimic.vhackos.botnet.service.base.IService;
import cz.ophite.mimic.vhackos.botnet.service.base.Service;
import cz.ophite.mimic.vhackos.botnet.shared.dto.MissionFinishedType;
import cz.ophite.mimic.vhackos.botnet.shared.injection.Autowired;
import cz.ophite.mimic.vhackos.botnet.shared.injection.Inject;
import cz.ophite.mimic.vhackos.botnet.shared.utils.SharedUtils;

/**
 * Služba pro dokončení úkolů a sebrání odměny.
 *
 * @author mimic
 */
@Inject
@EndpointService(IService.SERVICE_MISSION)
public final class MissionService extends Service {

    @Autowired
    private MissionsModule missionsModule;

    protected MissionService(Botnet botnet) {
        super(botnet);
    }

    @Override
    public String getDescription() {
        return "Completes the mission and collects rewards";
    }

    @Override
    protected void initialize() {
        setTimeout(getConfig().getMissionTimeout());
    }

    @Override
    protected void execute() {
        if (!SharedUtils.toBoolean(getShared().getUpdateResponse().getMissions())) {
            getLog().info("Missions are not available. Next check will be in: {}", SharedUtils
                    .toTimeFormat(getTimeout()));
            return;
        }
        var resp = missionsModule.getMissions();

        if (resp.getClaimNextDay() == 0) {
            sleep();
            resp = missionsModule.claimDaily();
            getLog().info("The daily reward is collected. {}xp, {} Boosters, {} Netcoins", resp
                    .getRewardExperience(), resp.getRewardBoosters(), resp.getRewardNetcoins());
        }
        for (var i = 0; i < resp.getDaily().size(); i++) {
            var mission = resp.getDaily().get(i);
            var type = MissionFinishedType.getByCode(mission.getFinished());

            if (type == MissionFinishedType.READY) {
                sleep();
                resp = missionsModule.claimMissionReward(i);
                getLog().info("Mission #{} has been completed. The reward is: {}xp + {} {}", i, mission
                        .getExperience(), mission.getRewardAmount(), mission.getRewardType());
            }
        }
        getLog().info("Done. Next check will be in: {}", SharedUtils.toTimeFormat(getTimeout()));
    }
}
