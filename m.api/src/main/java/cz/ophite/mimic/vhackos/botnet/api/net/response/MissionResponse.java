package cz.ophite.mimic.vhackos.botnet.api.net.response;

import cz.ophite.mimic.vhackos.botnet.api.net.response.base.Response;
import cz.ophite.mimic.vhackos.botnet.api.net.response.base.ResponseKey;
import cz.ophite.mimic.vhackos.botnet.api.net.response.data.MissionItemData;
import cz.ophite.mimic.vhackos.botnet.shared.ascii.AsciiRow;
import cz.ophite.mimic.vhackos.botnet.shared.ascii.converter.AsciiBooleanConverter;
import cz.ophite.mimic.vhackos.botnet.shared.ascii.converter.AsciiElapsedSecondsTimeConverter;

import java.util.List;

/**
 * Informace o misích.
 *
 * @author mimic
 */
public final class MissionResponse extends Response {

    @AsciiRow("Stage")
    @ResponseKey("stage")
    private Integer stage;
    public static final String P_STAGE = "stage";

    @AsciiRow(value = "Claim", converter = AsciiBooleanConverter.class)
    @ResponseKey("claim")
    private Integer claim;
    public static final String P_CLAIM = "claim";

    @AsciiRow(value = "Claimed", converter = AsciiBooleanConverter.class)
    @ResponseKey("claimed")
    private Integer claimed;
    public static final String P_CLAIMED = "claimed";

    @AsciiRow(value = "Claim Next Day", converter = AsciiElapsedSecondsTimeConverter.class)
    @ResponseKey("claimNextDay")
    private Long claimNextDay;
    public static final String P_CLAIM_NEXT_DAY = "claimNextDay";

    @AsciiRow(value = "Next Daily Reset", converter = AsciiElapsedSecondsTimeConverter.class)
    @ResponseKey("nextDailyReset")
    private Long nextDailyReset;
    public static final String P_NEXT_DAILY_RESET = "nextDailyReset";

    @AsciiRow("Daily Count")
    @ResponseKey("dailyCount")
    private Integer dailyCount;
    public static final String P_DAILY_COUNT = "dailyCount";

    @AsciiRow("Reward Boosters")
    @ResponseKey("rewBoosters")
    private Integer rewardBoosters;
    public static final String P_REWARD_BOOSTERS = "rewardBoosters";

    @AsciiRow("Reward Experience")
    @ResponseKey("rewExp")
    private Integer rewardExperience;
    public static final String P_REWARD_EXPERIENCE = "rewardExperience";

    @AsciiRow("Reward Netcoins")
    @ResponseKey("rewNetCoins")
    private Integer rewardNetcoins;
    public static final String P_REWARD_NETCOINS = "rewardNetcoins";

    @AsciiRow("Daily")
    @ResponseKey("daily")
    private List<MissionItemData> daily;
    public static final String P_DAILY = "daily";

    public Integer getStage() {
        return stage;
    }

    public void setStage(Integer stage) {
        this.stage = stage;
    }

    public Integer getClaim() {
        return claim;
    }

    public void setClaim(Integer claim) {
        this.claim = claim;
    }

    public Integer getClaimed() {
        return claimed;
    }

    public void setClaimed(Integer claimed) {
        this.claimed = claimed;
    }

    public Long getClaimNextDay() {
        return claimNextDay;
    }

    public void setClaimNextDay(Long claimNextDay) {
        this.claimNextDay = claimNextDay;
    }

    public Long getNextDailyReset() {
        return nextDailyReset;
    }

    public void setNextDailyReset(Long nextDailyReset) {
        this.nextDailyReset = nextDailyReset;
    }

    public Integer getDailyCount() {
        return dailyCount;
    }

    public void setDailyCount(Integer dailyCount) {
        this.dailyCount = dailyCount;
    }

    public List<MissionItemData> getDaily() {
        return daily;
    }

    public void setDaily(List<MissionItemData> daily) {
        this.daily = daily;
    }

    public Integer getRewardBoosters() {
        return rewardBoosters;
    }

    public void setRewardBoosters(Integer rewardBoosters) {
        this.rewardBoosters = rewardBoosters;
    }

    public Integer getRewardExperience() {
        return rewardExperience;
    }

    public void setRewardExperience(Integer rewardExperience) {
        this.rewardExperience = rewardExperience;
    }

    public Integer getRewardNetcoins() {
        return rewardNetcoins;
    }

    public void setRewardNetcoins(Integer rewardNetcoins) {
        this.rewardNetcoins = rewardNetcoins;
    }
}
