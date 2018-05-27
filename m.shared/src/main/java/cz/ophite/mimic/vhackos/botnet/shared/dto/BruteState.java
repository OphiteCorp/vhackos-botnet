package cz.ophite.mimic.vhackos.botnet.shared.dto;

/**
 * Stav bruteforce IP.
 *
 * @author mimic
 */
public enum BruteState {

    RUNNING(0, "Running"),
    FAILED(2, "Failed"),
    SUCCESS(1, "Success");

    private final String alias;
    private final int state;

    BruteState(int state, String alias) {
        this.state = state;
        this.alias = alias;
    }

    public String getAlias() {
        return alias;
    }

    /**
     * Získá typ podle stavu.
     */
    public static BruteState getbyState(int state) {
        for (var s : values()) {
            if (s.state == state) {
                return s;
            }
        }
        return null;
    }
}
