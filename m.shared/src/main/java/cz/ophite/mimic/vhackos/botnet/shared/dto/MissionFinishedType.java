package cz.ophite.mimic.vhackos.botnet.shared.dto;

/**
 * Typ dokončení mise.
 *
 * @author mimic
 */
public enum MissionFinishedType {

    INACTIVE(0, "Inactive"),
    READY(1, "Ready"),
    FINISHED(2, "Finished");

    private final int code;
    private final String alias;

    MissionFinishedType(int code, String alias) {
        this.code = code;
        this.alias = alias;
    }

    public String getAlias() {
        return alias;
    }

    public static MissionFinishedType getByCode(int code) {
        for (var type : values()) {
            if (type.code == code) {
                return type;
            }
        }
        return null;
    }
}
