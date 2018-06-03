package cz.ophite.mimic.vhackos.botnet.config;

import java.util.regex.Pattern;

/**
 * Zpracuje výchozí hodnoty a hodnoty z konfigurace.
 *
 * @author mimic
 */
final class ConfigValueProcessor {

    private static final Pattern PATTERN_F_RAND = Pattern.compile("f_rand\\(\\s*(\\d+)\\s*,\\s*(\\d+)\\s*\\)");
    private static final Pattern PATTERN_F_RMSEC = Pattern.compile("f_rmsec\\(\\)");

    /**
     * Zpracuje hodnotu.
     */
    static String process(String value) {
        var m = PATTERN_F_RAND.matcher(value);
        if (m.find() && m.groupCount() == 2) {
            var min = Long.valueOf(m.group(1));
            var max = Long.valueOf(m.group(2)) - min + 1;
            value = m.replaceAll(String.format("(Math.floor(Math.random()*%s)+%s)", max, min));
        }
        m = PATTERN_F_RMSEC.matcher(value);
        if (m.find()) {
            value = m.replaceAll(String.format("(Math.floor(Math.random()*%s)+%s)", 1000, 0));
        }
        return value;
    }
}
