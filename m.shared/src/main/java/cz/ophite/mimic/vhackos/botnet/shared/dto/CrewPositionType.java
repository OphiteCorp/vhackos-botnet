package cz.ophite.mimic.vhackos.botnet.shared.dto;

/**
 * Úmístění v crew.
 *
 * @author mimic
 */
public enum CrewPositionType {

    FOUNDER(3, "Founder"),
    CO_FOUNDER(2, "Co-Founder"),
    MEMBER(1, "Member");

    private final String alias;
    private final int position;

    CrewPositionType(int position, String alias) {
        this.position = position;
        this.alias = alias;
    }

    public String getAlias() {
        return alias;
    }

    /**
     * Získá typ podle pozice.
     */
    public static CrewPositionType getbyPosition(int position) {
        for (var s : values()) {
            if (s.position == position) {
                return s;
            }
        }
        return null;
    }
}
