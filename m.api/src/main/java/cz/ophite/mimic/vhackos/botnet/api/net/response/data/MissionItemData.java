package cz.ophite.mimic.vhackos.botnet.api.net.response.data;

import cz.ophite.mimic.vhackos.botnet.api.net.response.base.ResponseKey;
import cz.ophite.mimic.vhackos.botnet.shared.utils.ascii.AsciiRow;
import cz.ophite.mimic.vhackos.botnet.shared.utils.ascii.converter.AsciiMissionFinishedTypeConverter;

/**
 * Informace o misi.
 *
 * @author mimic
 */
public final class MissionItemData {

    @AsciiRow("Title")
    @ResponseKey(KEY_TITLE)
    private String title;
    public static final String P_TITLE = "title";
    private static final String KEY_TITLE = "title";

    @AsciiRow("Description")
    @ResponseKey(KEY_DESCRIPTION)
    private String description;
    public static final String P_DESCRIPTION = "description";
    private static final String KEY_DESCRIPTION = "descr";

    @AsciiRow(value = "Finished", converter = AsciiMissionFinishedTypeConverter.class)
    @ResponseKey(KEY_FINISHED)
    private Integer finished;
    public static final String P_FINISHED = "finished";
    private static final String KEY_FINISHED = "finished";

    @AsciiRow("Experience")
    @ResponseKey(KEY_EXPERIENCE)
    private Integer experience;
    public static final String P_EXPERIENCE = "experience";
    private static final String KEY_EXPERIENCE = "exp";

    @AsciiRow("Reward Type")
    @ResponseKey(KEY_REWARD_TYPE)
    private String rewardType;
    public static final String P_REWARD_TYPE = "rewardType";
    private static final String KEY_REWARD_TYPE = "rewType";

    @AsciiRow("Reward")
    @ResponseKey(KEY_REWARD_AMOUNT)
    private Integer rewardAmount;
    public static final String P_REWARD_AMOUNT = "rewardAmount";
    private static final String KEY_REWARD_AMOUNT = "rewAmount";

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getFinished() {
        return finished;
    }

    public void setFinished(Integer finished) {
        this.finished = finished;
    }

    public Integer getExperience() {
        return experience;
    }

    public void setExperience(Integer experience) {
        this.experience = experience;
    }

    public String getRewardType() {
        return rewardType;
    }

    public void setRewardType(String rewardType) {
        this.rewardType = rewardType;
    }

    public Integer getRewardAmount() {
        return rewardAmount;
    }

    public void setRewardAmount(Integer rewardAmount) {
        this.rewardAmount = rewardAmount;
    }
}
