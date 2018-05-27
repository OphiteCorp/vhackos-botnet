package cz.ophite.mimic.vhackos.botnet.utils;

/**
 * Pomocné metody.
 *
 * @author mimic
 */
public final class Utils {

    /**
     * Vyhodnotí, zda je IP platná.
     */
    public static boolean isValidIp(String ip) {
        return (ip != null && !ip.equals("ANONYMOUS") && !ip.equals("???"));
    }
}
