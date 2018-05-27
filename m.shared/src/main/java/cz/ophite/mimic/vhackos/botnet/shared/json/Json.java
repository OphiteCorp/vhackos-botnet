package cz.ophite.mimic.vhackos.botnet.shared.json;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.List;
import java.util.Map;

/**
 * Objekt JSON, který umožňuje převody mezi JSON a jiným datovým typem.
 *
 * @author mimic
 */
public final class Json {

    private static final Logger LOG = LoggerFactory.getLogger(Json.class);

    private static final Gson GSON;

    static {
        GsonBuilder builder = new GsonBuilder();
        builder.serializeNulls();
        builder.setPrettyPrinting();
        builder.registerTypeAdapter(new TypeToken<Map<String, Object>>() {
        }.getType(), new JsonDeserializeMapStrategy());
        GSON = builder.create();
    }

    /**
     * Převede objekt do JSON.
     */
    public static String toJson(Object obj) {
        return GSON.toJson(obj);
    }

    /**
     * Převede JSON na vlastní objekt.
     */
    public static <T> T toObject(String json, Class<T> clazz) {
        return GSON.fromJson(json, clazz);
    }

    /**
     * Převede JSON na vlastní objekt podle typu.
     */
    public static <T> T toObjectByType(String json, TypeToken<T> typeToken) {
        return GSON.fromJson(json, typeToken.getType());
    }

    /**
     * Převede JSON do mapy.
     */
    public static Map<String, Object> toMap(String json) {
        return GSON.fromJson(json, new TypeToken<Map<String, Object>>() {
        }.getType());
    }

    /**
     * Převede JSON do listu.
     */
    public static <T> List<T> toList(String json, Class<T> clazz) {
        return GSON.fromJson(json, new TypeToken<List<T>>() {
        }.getType());
    }

    /**
     * Načte soubor v podobě JSON.
     */
    public static <T> T toObject(File file, Class<T> clazz) {
        try (var reader = new JsonReader(new FileReader(file))) {
            Object obj = GSON.fromJson(reader, clazz);
            LOG.debug("The JSON file '{}' has been loaded", file.getName());
            return (T) obj;

        } catch (FileNotFoundException e) {
            LOG.debug("The file '{}' does not yet exist", file.getName());
            return null;

        } catch (IOException e) {
            LOG.error("There was an error reading json file: " + file.getName(), e);
            return null;
        }
    }

    /**
     * Uloží data do JSON souboru.
     */
    public static String toFile(String outFile, Object obj) {
        if (outFile == null || obj == null) {
            return null;
        }
        try (FileWriter fw = new FileWriter(new File(outFile))) {
            String json = GSON.toJson(obj);
            fw.write(json);
            LOG.debug("Object '{}' saved to JSON file", obj.getClass().getSimpleName());
            return json;

        } catch (IOException e) {
            LOG.error("There was an error writing the object '" + obj.getClass().getSimpleName() + "' to the file", e);
        }
        return null;
    }
}
