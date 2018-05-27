package cz.ophite.mimic.vhackos.botnet.api.net.response;

import cz.ophite.mimic.vhackos.botnet.api.net.response.base.Response;
import cz.ophite.mimic.vhackos.botnet.api.net.response.base.ResponseKey;
import cz.ophite.mimic.vhackos.botnet.api.net.response.data.IpBruteDetailData;
import cz.ophite.mimic.vhackos.botnet.api.net.response.data.TaskUpdateData;
import cz.ophite.mimic.vhackos.botnet.shared.utils.ascii.AsciiRow;
import cz.ophite.mimic.vhackos.botnet.shared.utils.ascii.converter.AsciiBooleanConverter;
import cz.ophite.mimic.vhackos.botnet.shared.utils.ascii.converter.AsciiElapsedSecondsTimeConverter;

import java.util.List;

/**
 * Odpověď serveru na získání tásků.
 *
 * @author mimic
 */
public final class TaskResponse extends Response {

    @AsciiRow(value = "Finish All", converter = AsciiBooleanConverter.class)
    @ResponseKey("finishall")
    private Integer finishAll;
    public static final String P_FINISH_ALL = "finishAll";

    @AsciiRow(value = "Aborted", converter = AsciiBooleanConverter.class)
    @ResponseKey("aborted")
    private Integer aborted;
    public static final String P_ABORTED = "aborted";

    @AsciiRow(value = "Brute Removed", converter = AsciiBooleanConverter.class)
    @ResponseKey("bruteremoved")
    private Integer bruteRemoved;
    public static final String P_BRUTE_REMOVED = "bruteRemoved";

    @AsciiRow(value = "Brute Aborted", converter = AsciiBooleanConverter.class)
    @ResponseKey("bruteaborted")
    private Integer bruteAborted;
    public static final String P_BRUTE_ABORTED = "bruteAborted";

    @AsciiRow(value = "Brute Finished", converter = AsciiBooleanConverter.class)
    @ResponseKey("brutefinished")
    private Integer bruteFinished;
    public static final String P_BRUTE_FINISHED = "bruteFinished";

    @AsciiRow(value = "Finished", converter = AsciiBooleanConverter.class)
    @ResponseKey("finished")
    private Integer finished;
    public static final String P_FINISHED = "finished";

    @AsciiRow(value = "Brute Retry", converter = AsciiBooleanConverter.class)
    @ResponseKey("bruteretry")
    private Integer bruteRetry;
    public static final String P_BRUTE_RETRY = "bruteRetry";

    @AsciiRow("Boosted")
    @ResponseKey("boosted")
    private Integer boosted;
    public static final String P_BOOSTED = "boosted";

    @AsciiRow(value = "Level Up", converter = AsciiBooleanConverter.class)
    @ResponseKey("lvlup")
    private Integer levelUp;
    public static final String P_LEVEL_UP = "levelUp";

    @AsciiRow("Update Count")
    @ResponseKey("updateCount")
    private Integer updateCount;
    public static final String P_UPDATE_COUNT = "updateCount";

    @AsciiRow("Finish All Costs")
    @ResponseKey("finishallcosts")
    private Integer finishAllCosts;
    public static final String P_FINISH_ALL_COSTS = "finishAllCosts";

    @AsciiRow("Brute Count")
    @ResponseKey("bruteCount")
    private Integer bruteCount;
    public static final String P_BRUTE_COUNT = "bruteCount";

    @AsciiRow(value = "Next Done", converter = AsciiElapsedSecondsTimeConverter.class)
    @ResponseKey("nextdone")
    private Long nextDone;
    public static final String P_NEXT_DONE = "nextDone";

    @AsciiRow(value = "Next Done 2", converter = AsciiElapsedSecondsTimeConverter.class)
    @ResponseKey("nextdone2")
    private Long nextDone2;
    public static final String P_NEXT_DONE_2 = "nextDone2";

    @AsciiRow("Level")
    @ResponseKey("level")
    private Integer level;
    public static final String P_LEVEL = "level";

    @AsciiRow("NetCoins")
    @ResponseKey("netcoins")
    private Integer netCoins;
    public static final String P_NETCOINS = "netCoins";

    @AsciiRow("Boosters")
    @ResponseKey("boosters")
    private Integer boosters;
    public static final String P_BOOSTERS = "boosters";

    @AsciiRow("Bruted IPs")
    @ResponseKey(value = "brutes")
    private List<IpBruteDetailData> brutedIps;
    public static final String P_BRUTED_IPS = "brutedIps";

    @AsciiRow("Updates")
    @ResponseKey("updates")
    private List<TaskUpdateData> updates;
    public static final String P_UPDATES = "updates";

    public Integer getFinishAll() {
        return finishAll;
    }

    public void setFinishAll(Integer finishAll) {
        this.finishAll = finishAll;
    }

    public Integer getAborted() {
        return aborted;
    }

    public void setAborted(Integer aborted) {
        this.aborted = aborted;
    }

    public Integer getBruteRemoved() {
        return bruteRemoved;
    }

    public void setBruteRemoved(Integer bruteRemoved) {
        this.bruteRemoved = bruteRemoved;
    }

    public Integer getBruteAborted() {
        return bruteAborted;
    }

    public void setBruteAborted(Integer bruteAborted) {
        this.bruteAborted = bruteAborted;
    }

    public Integer getBruteFinished() {
        return bruteFinished;
    }

    public void setBruteFinished(Integer bruteFinished) {
        this.bruteFinished = bruteFinished;
    }

    public Integer getFinished() {
        return finished;
    }

    public void setFinished(Integer finished) {
        this.finished = finished;
    }

    public Integer getBruteRetry() {
        return bruteRetry;
    }

    public void setBruteRetry(Integer bruteRetry) {
        this.bruteRetry = bruteRetry;
    }

    public Integer getBoosted() {
        return boosted;
    }

    public void setBoosted(Integer boosted) {
        this.boosted = boosted;
    }

    public Integer getLevelUp() {
        return levelUp;
    }

    public void setLevelUp(Integer levelUp) {
        this.levelUp = levelUp;
    }

    public Integer getUpdateCount() {
        return updateCount;
    }

    public void setUpdateCount(Integer updateCount) {
        this.updateCount = updateCount;
    }

    public Integer getBruteCount() {
        return bruteCount;
    }

    public void setBruteCount(Integer bruteCount) {
        this.bruteCount = bruteCount;
    }

    public Long getNextDone2() {
        return nextDone2;
    }

    public void setNextDone2(Long nextDone2) {
        this.nextDone2 = nextDone2;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getNetCoins() {
        return netCoins;
    }

    public void setNetCoins(Integer netCoins) {
        this.netCoins = netCoins;
    }

    public Integer getBoosters() {
        return boosters;
    }

    public void setBoosters(Integer boosters) {
        this.boosters = boosters;
    }

    public List<IpBruteDetailData> getBrutedIps() {
        return brutedIps;
    }

    public void setBrutedIps(List<IpBruteDetailData> brutedIps) {
        this.brutedIps = brutedIps;
    }

    public Integer getFinishAllCosts() {
        return finishAllCosts;
    }

    public void setFinishAllCosts(Integer finishAllCosts) {
        this.finishAllCosts = finishAllCosts;
    }

    public Long getNextDone() {
        return nextDone;
    }

    public void setNextDone(Long nextDone) {
        this.nextDone = nextDone;
    }

    public List<TaskUpdateData> getUpdates() {
        return updates;
    }

    public void setUpdates(List<TaskUpdateData> updates) {
        this.updates = updates;
    }
}
