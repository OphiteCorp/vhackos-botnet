package cz.ophite.mimic.vhackos.botnet.shared.json;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.internal.LinkedTreeMap;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Strategie pro deserializaci JSON do mapy.
 *
 * @author mimic
 */
public final class JsonDeserializeMapStrategy implements JsonDeserializer<Map<String, Object>> {

    @Override
    public Map<String, Object> deserialize(JsonElement jsonElement, Type type,
            JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {

        var value = read(jsonElement);

        if (value instanceof Map) {
            return (Map<String, Object>) value;
        } else {
            var map = new HashMap<String, Object>();
            map.put(null, value);
            return map;
        }
    }

    private Object read(JsonElement jsonElement) {
        if (jsonElement.isJsonArray()) {
            var list = new ArrayList<>();
            var jsonArray = jsonElement.getAsJsonArray();

            for (var element : jsonArray) {
                list.add(read(element));
            }
            return list;

        } else if (jsonElement.isJsonObject()) {
            var map = new LinkedTreeMap<String, Object>();
            var obj = jsonElement.getAsJsonObject();
            var entitySet = obj.entrySet();

            for (var entry : entitySet) {
                map.put(entry.getKey(), read(entry.getValue()));
            }
            return map;

        } else if (jsonElement.isJsonPrimitive()) {
            var primitive = jsonElement.getAsJsonPrimitive();

            if (primitive.isBoolean()) {
                return primitive.getAsBoolean();

            } else if (primitive.isString()) {
                return primitive.getAsString();

            } else if (primitive.isNumber()) {
                var number = primitive.getAsNumber();
                var longValue = number.longValue();
                var doubleValue = number.doubleValue();

                if (Math.ceil(number.doubleValue()) == longValue) {
                    if (longValue > Integer.MAX_VALUE) {
                        return longValue;
                    }
                    if (longValue > Short.MAX_VALUE) {
                        return number.intValue();
                    }
                    if (longValue > Byte.MAX_VALUE) {
                        return number.shortValue();
                    }
                    return number.byteValue();
                } else {
                    // musí být vždy double, protože jinak ořízne desetinná místa
                    return doubleValue;
                }
            }
        }
        return null;
    }
}
