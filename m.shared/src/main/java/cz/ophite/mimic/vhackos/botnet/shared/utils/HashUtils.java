package cz.ophite.mimic.vhackos.botnet.shared.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Pomocné metody pro hashování.
 *
 * @author mimic
 */
public final class HashUtils {

    private static final Logger LOG = LoggerFactory.getLogger(HashUtils.class);

    /**
     * Metoda přebrána z vHackOS.<br>
     * Vytvoří MD5 hash přes hex převod.
     */
    public static String toHexMd5(String string) {
        try {
            var digest = MessageDigest.getInstance("MD5");
            digest.update(string.getBytes());

            var messageDigest = digest.digest();
            var hexString = new StringBuilder();

            for (var b : messageDigest) {
                var sb = new StringBuilder(Integer.toHexString(b & 255));

                while (sb.length() < 2) {
                    sb.insert(0, "0");
                }
                hexString.append(sb);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            LOG.error("Invalid digest method", e);
            return "";
        }
    }
}
