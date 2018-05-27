package cz.ophite.mimic.vhackos.botnet.shared.utils;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

/**
 * Pomocné metody pro práci s API.
 *
 * @author mimic
 */
public final class SharedUtils {

    private static final ScriptEngineManager SEM = new ScriptEngineManager();
    private static final ScriptEngine SE = SEM.getEngineByName("js");

    /**
     * Vyhodnotí výraz a vrátí výsledek.
     */
    public static <T> T eval(String script) {
        try {
            return (T) SE.eval(script);
        } catch (ScriptException e) {
            throw new RuntimeException("An error occurred while evaluating an expression: " + script, e);
        }
    }

    /**
     * Převede objekt na formátovaný boolean "Yes/No". Null hodnota vrací "No". Číselná hodnota větší než 0 vrací "Yes".
     */
    public static String convertToBoolean(Object value) {
        return toBoolean(value) ? "Yes" : "No";
    }

    /**
     * Převede objekt na boolean. Null hodnota vrací false. Číselná hodnota větší než 0 vrací true.
     */
    public static boolean toBoolean(Object value) {
        if (value == null) {
            return false;
        }
        if (value instanceof Number) {
            value = Integer.valueOf(value.toString()) > 0;
        }
        return Boolean.valueOf(value.toString());
    }

    /**
     * Naformátuje peníze.
     */
    public static String toMoneyFormat(Object money) {

        if (money == null) {
            money = "0";
        } else {
            money = money.toString();
        }
        return String.format("$%,d", Long.parseLong(money.toString())).replace(".", ",");
    }

    /**
     * Naformátuje čas v milisekundách.
     */
    public static String toTimeFormat(long millis) {
        var sb = new StringBuilder();

        long days = millis / 86400000;
        if (days > 0) {
            millis %= 86400000;
            sb.append(String.format("%dd ", days)); // min 14
        }
        long hours = millis / 3600000;
        if (hours > 0) {
            millis %= 3600000;
            sb.append(String.format("%02dh ", hours)); // max 11
        } else {
            if (days > 0) {
                sb.append("00h ");
            }
        }
        long minutes = millis / 60000;
        if (minutes > 0) {
            millis %= 60000;
            sb.append(String.format("%02dm ", minutes)); // max 7
        } else {
            if (hours > 0) {
                sb.append("00m ");
            }
        }
        long seconds = millis / 1000;
        if (seconds > 0) {
            sb.append(String.format("%02ds", seconds)); // max 3
        } else {
            if (hours > 0) {
                sb.append("00s");
            }
        }
        var out = sb.toString();
        if (out.isEmpty()) {
            out = "0s";
        }
        return out;
    }
}
