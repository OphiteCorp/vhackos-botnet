package cz.ophite.mimic.vhackos.botnet.api.net.response;

import cz.ophite.mimic.vhackos.botnet.api.net.response.base.Response;
import cz.ophite.mimic.vhackos.botnet.api.net.response.base.ResponseKey;
import cz.ophite.mimic.vhackos.botnet.shared.utils.ascii.AsciiRow;
import cz.ophite.mimic.vhackos.botnet.shared.utils.ascii.converter.AsciiBooleanConverter;
import cz.ophite.mimic.vhackos.botnet.shared.utils.ascii.converter.AsciiElapsedSecondsTimeConverter;
import cz.ophite.mimic.vhackos.botnet.shared.utils.ascii.converter.AsciiMinerStateConverter;

/**
 * Informace o netcoin mineru.
 *
 * @author mimic
 */
public final class MiningResponse extends Response {

    @AsciiRow(value = "Upgraded", converter = AsciiBooleanConverter.class)
    @ResponseKey("upgraded")
    private Integer upgraded;
    public static final String P_UPGRADED = "upgraded";

    @AsciiRow(value = "Running", converter = AsciiMinerStateConverter.class)
    @ResponseKey("running")
    private Integer running;
    public static final String P_RUNNING = "running";

    @AsciiRow(value = "Applied", converter = AsciiBooleanConverter.class)
    @ResponseKey("applied")
    private Integer applied;
    public static final String P_APPLIED = "applied";

    @AsciiRow(value = "Claimed", converter = AsciiBooleanConverter.class)
    @ResponseKey("claimed")
    private Integer claimed;
    public static final String P_CLAIMED = "claimed";

    @AsciiRow(value = "Started", converter = AsciiBooleanConverter.class)
    @ResponseKey("started")
    private Integer started;
    public static final String P_STARTED = "started";

    @AsciiRow("Mined")
    @ResponseKey("mined")
    private Integer mined;
    public static final String P_MINED = "mined";

    @AsciiRow("NetCoins")
    @ResponseKey("netcoins")
    private Integer netCoins;
    public static final String P_NETCOINS = "netCoins";

    @AsciiRow("GPU Count")
    @ResponseKey("gpuCount")
    private Integer gpuCount;
    public static final String P_GPU_COUNT = "gpuCount";

    @AsciiRow(value = "Upgradable", converter = AsciiBooleanConverter.class)
    @ResponseKey("upgradable")
    private Integer upgradable;
    public static final String P_UPGRADABLE = "upgradable";

    @AsciiRow("Mining Speed")
    @ResponseKey("miningSpeed")
    private Double miningSpeed;
    public static final String P_MINING_SPEED = "miningSpeed";

    @AsciiRow("New GPU Costs")
    @ResponseKey("newGPUCosts")
    private Integer newGpuCosts;
    public static final String P_NEW_GPU_COSTS = "newGpuCosts";

    @AsciiRow(value = "Elapsed", converter = AsciiElapsedSecondsTimeConverter.class)
    @ResponseKey("is")
    private Long is;
    public static final String P_IS = "is";

    @AsciiRow(value = "Left", converter = AsciiElapsedSecondsTimeConverter.class)
    @ResponseKey("left")
    private Long left;
    public static final String P_LEFT = "left";

    @AsciiRow(value = "Need", converter = AsciiElapsedSecondsTimeConverter.class)
    @ResponseKey("need")
    private Long need;
    public static final String P_NEED = "need";

    @AsciiRow("Will Earn")
    @ResponseKey("willearn")
    private Integer willEarn;
    public static final String P_WILL_EARN = "willEarn";

    public Long getIs() {
        return is;
    }

    public void setIs(Long is) {
        this.is = is;
    }

    public Long getLeft() {
        return left;
    }

    public void setLeft(Long left) {
        this.left = left;
    }

    public Long getNeed() {
        return need;
    }

    public void setNeed(Long need) {
        this.need = need;
    }

    public Integer getWillEarn() {
        return willEarn;
    }

    public void setWillEarn(Integer willEarn) {
        this.willEarn = willEarn;
    }

    public Integer getNewGpuCosts() {
        return newGpuCosts;
    }

    public void setNewGpuCosts(Integer newGpuCosts) {
        this.newGpuCosts = newGpuCosts;
    }

    public Integer getUpgraded() {
        return upgraded;
    }

    public void setUpgraded(Integer upgraded) {
        this.upgraded = upgraded;
    }

    public Integer getRunning() {
        return running;
    }

    public void setRunning(Integer running) {
        this.running = running;
    }

    public Integer getApplied() {
        return applied;
    }

    public void setApplied(Integer applied) {
        this.applied = applied;
    }

    public Integer getClaimed() {
        return claimed;
    }

    public void setClaimed(Integer claimed) {
        this.claimed = claimed;
    }

    public Integer getStarted() {
        return started;
    }

    public void setStarted(Integer started) {
        this.started = started;
    }

    public Integer getMined() {
        return mined;
    }

    public void setMined(Integer mined) {
        this.mined = mined;
    }

    public Integer getNetCoins() {
        return netCoins;
    }

    public void setNetCoins(Integer netCoins) {
        this.netCoins = netCoins;
    }

    public Integer getGpuCount() {
        return gpuCount;
    }

    public void setGpuCount(Integer gpuCount) {
        this.gpuCount = gpuCount;
    }

    public Integer getUpgradable() {
        return upgradable;
    }

    public void setUpgradable(Integer upgradable) {
        this.upgradable = upgradable;
    }

    public Double getMiningSpeed() {
        return miningSpeed;
    }

    public void setMiningSpeed(Double miningSpeed) {
        this.miningSpeed = miningSpeed;
    }
}
