package cz.ophite.mimic.vhackos.botnet.api.net.response.data;

import cz.ophite.mimic.vhackos.botnet.api.net.response.base.ResponseKey;
import cz.ophite.mimic.vhackos.botnet.shared.utils.ascii.AsciiRow;
import cz.ophite.mimic.vhackos.botnet.shared.utils.ascii.converter.AsciiAppStoreTypeConverter;

/**
 * Informace o aktualizaci aplikace v táscích.
 *
 * @author mimic
 */
public final class TaskUpdateData {

    @AsciiRow("Task ID")
    @ResponseKey(KEY_TASK_ID)
    private Long taskId;
    public static final String P_TASK_ID = "taskId";
    private static final String KEY_TASK_ID = "id";

    @AsciiRow(value = "App ID", converter = AsciiAppStoreTypeConverter.class)
    @ResponseKey(KEY_APP_ID)
    private Integer appId;
    public static final String P_APP_ID = "appId";
    private static final String KEY_APP_ID = "appid";

    @AsciiRow("Start")
    @ResponseKey(KEY_START)
    private Long start;
    public static final String P_START = "start";
    private static final String KEY_START = "start";

    @AsciiRow("End")
    @ResponseKey(KEY_END)
    private Long end;
    public static final String P_END = "end";
    private static final String KEY_END = "end";

    @AsciiRow("Now")
    @ResponseKey(KEY_NOW)
    private Long now;
    public static final String P_NOW = "now";
    private static final String KEY_NOW = "now";

    @AsciiRow("Level")
    @ResponseKey(KEY_LEVEL)
    private Integer level;
    public static final String P_LEVEL = "level";
    private static final String KEY_LEVEL = "level";

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public Integer getAppId() {
        return appId;
    }

    public void setAppId(Integer appId) {
        this.appId = appId;
    }

    public Long getStart() {
        return start;
    }

    public void setStart(Long start) {
        this.start = start;
    }

    public Long getEnd() {
        return end;
    }

    public void setEnd(Long end) {
        this.end = end;
    }

    public Long getNow() {
        return now;
    }

    public void setNow(Long now) {
        this.now = now;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }
}
