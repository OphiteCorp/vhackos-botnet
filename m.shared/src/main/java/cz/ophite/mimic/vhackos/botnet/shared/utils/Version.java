package cz.ophite.mimic.vhackos.botnet.shared.utils;

/**
 * Informace o verzi.
 *
 * @author mimic
 */
public final class Version {

    private int major;
    private int minor;
    private int update;

    private Version() {
    }

    public static Version create(String version) {
        var tokens = version.split("\\.");
        var v = new Version();
        v.major = Integer.valueOf(tokens[0]);
        v.minor = Integer.valueOf(tokens[1]);
        v.update = Integer.valueOf(tokens[2]);
        return v;
    }

    public boolean isInputHigher(Version version) {
        if (version.major > major) {
            return true;
        }
        if (version.minor > minor) {
            return true;
        }
        return (version.update > update);
    }

    @Override
    public String toString() {
        return String.format("%s.%s.%s", major, minor, update);
    }

    private long makeSum() {
        return (long) ((major * 1e6) + (minor * 1e4) + (update * 1e2));
    }
}
