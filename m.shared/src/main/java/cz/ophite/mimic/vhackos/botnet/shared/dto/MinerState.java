package cz.ophite.mimic.vhackos.botnet.shared.dto;

/**
 * Stav těžby netcoins.
 *
 * @author mimic
 */
public enum MinerState {

    STOPPED(0, "Stopped"),
    RUNNING(1, "Running"),
    FINISHED(2, "Finished");

    private final int code;
    private final String alias;

    MinerState(int code, String alias) {
        this.code = code;
        this.alias = alias;
    }

    public int getCode() {
        return code;
    }

    public String getAlias() {
        return alias;
    }

    public static MinerState getByCode(int code) {
        for (var state : values()) {
            if (state.code == code) {
                return state;
            }
        }
        return null;
    }
}
