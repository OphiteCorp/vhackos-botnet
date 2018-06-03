package cz.ophite.mimic.vhackos.botnet.config;

import cz.ophite.mimic.vhackos.botnet.shared.utils.SharedUtils;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Pomocné metody pro práci s konfigurací.
 *
 * @author mimic
 */
public final class ConfigHelper {

    /**
     * Převede textovou hodnotu na boolean.
     */
    static boolean getBoolean(String value) {
        if (value != null && !value.isEmpty()) {
            if ("yes".equalsIgnoreCase(value)) {
                return true;
            }
            return Boolean.valueOf(value);
        }
        return false;
    }

    /**
     * Převede textohou hodnotu na číslo.
     */
    static <T> T getNumbericValue(String value, Class<T> target) {
        value = ConfigValueProcessor.process(value);
        var result = SharedUtils.eval(value);
        var out = result;

        if (result instanceof Double && target != Double.class) {
            var dbl = (Double) result;

            if (target == Long.class) {
                out = dbl.longValue();

            } else if (target == Integer.class) {
                out = dbl.intValue();
            }
        } else if (out.getClass() != target) {
            if (result instanceof Integer && target == Long.class) {
                out = Long.valueOf(result.toString());
            }
        }
        return (T) out;
    }

    /**
     * Získá konfiguraci jako mapu.
     */
    public static Map<String, Object> asMap(ApplicationConfig config) {
        var map = new LinkedHashMap<String, Object>();
        var fields = config.getClass().getDeclaredFields();

        for (var field : fields) {
            if (field.isAnnotationPresent(ConfigValue.class)) {
                field.setAccessible(true);
                var a = field.getAnnotation(ConfigValue.class);

                try {
                    var value = field.get(config);
                    map.put(a.value(), value);

                } catch (Exception e) {
                    throw new IllegalStateException("An unexpected error has occurred", e);
                }
            }
        }
        return map;
    }
}
