package cz.ophite.mimic.vhackos.botnet.api.opcode.base;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Zákadní implementace opcode.
 *
 * @author mimic
 */
public abstract class Opcode implements IOpcode {

    // stačí 5 parametu, víc snad nikde nebude
    private final Map<String, String> params = new LinkedHashMap<>(5);

    /**
     * Přidá parametr s hodnotou.
     */
    protected final void addParam(String paramName, String value) {
        params.put(paramName, value);
    }

    @Override
    public final Map<String, String> getParams() {
        return params;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("params", params).toString();
    }
}
