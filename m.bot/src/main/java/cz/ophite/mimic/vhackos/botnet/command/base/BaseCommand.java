package cz.ophite.mimic.vhackos.botnet.command.base;

import cz.ophite.mimic.vhackos.botnet.Botnet;
import cz.ophite.mimic.vhackos.botnet.Constants;
import cz.ophite.mimic.vhackos.botnet.api.exception.BotnetCoreException;
import cz.ophite.mimic.vhackos.botnet.shared.ascii.AsciiMaker;
import cz.ophite.mimic.vhackos.botnet.shared.ascii.AsciiRow;
import cz.ophite.mimic.vhackos.botnet.shared.utils.AsciiUtils;
import cz.ophite.mimic.vhackos.botnet.shared.utils.SentryGuard;
import de.vandermeer.asciitable.AT_Row;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.TreeMap;

/**
 * Základní deklarace příkazu.
 *
 * @author mimic
 */
public abstract class BaseCommand {

    public static final String COMMAND_PACKAGE = Constants.BASE_PACKAGE + ".command";

    private final Logger log;
    private final Botnet botnet;

    protected BaseCommand(Botnet botnet) {
        this.botnet = botnet;
        log = LoggerFactory.getLogger(getClass());
    }

    protected static AT_Row put(AsciiMaker am, Object key, Object value) {
        if (value != null) {
            return am.add(key, fixValue(value));
        }
        return null;
    }

    protected static AT_Row put(AsciiMaker am, Map.Entry<String, FieldData> data) {
        if (data.getValue() != null) {
            return am.add(data.getValue().name, fixValue(data.getValue().value));
        }
        return null;
    }

    protected static AT_Row put(AsciiMaker am, FieldData data) {
        if (data != null) {
            return am.add(data.name, fixValue(data.value));
        }
        return null;
    }

    protected static void putRemainings(AsciiMaker am, Map<String, FieldData> fields) {
        putRemainings(am, fields, true);
    }

    protected static void putRemainings(AsciiMaker am, Map<String, FieldData> fields, boolean addRule) {
        if (!fields.isEmpty()) {
            if (addRule) {
                am.addRule();
            }
            for (var entry : fields.entrySet()) {
                if (entry.getValue().value != null) {
                    put(am, entry);
                }
            }
        }
    }

    protected final Botnet getBotnet() {
        return botnet;
    }

    protected final Logger getLog() {
        return log;
    }

    protected final String execute(String title, ISafeCommand safeCommand) {
        var am = createAsciiMaker(title);
        try {
            safeCommand.execute(am);

        } catch (BotnetCoreException e) {
            log.debug("There was an error processing the command: " + title);
            am = createAsciiMaker(title);
            put(am, "Error", StringUtils.isEmpty(e.getMessage()) ? e.toString() : e.getMessage());

        } catch (Exception e) {
            SentryGuard.log(e);
            log.error("There was an error processing the command: " + title, e);
            am = createAsciiMaker(title);
            put(am, "Error", StringUtils.isEmpty(e.getMessage()) ? e.toString() : e.getMessage());
        }
        return renderAsciiMaker(am);
    }

    protected static Map<String, FieldData> getFields(Object obj, boolean onlyAsciiRows) {
        return getFields(obj, onlyAsciiRows, true);
    }

    protected static Map<String, FieldData> getFields(Object obj, boolean onlyAsciiRows, boolean skipNullValues) {
        var fields = obj.getClass().getDeclaredFields();
        var resultFields = new TreeMap<String, FieldData>();

        for (var field : fields) {
            if ((field.isAnnotationPresent(AsciiRow.class) && onlyAsciiRows) || !onlyAsciiRows) {
                var name = field.getName();
                var data = new FieldData();

                if (field.isAnnotationPresent(AsciiRow.class)) {
                    var a = field.getDeclaredAnnotation(AsciiRow.class);
                    name = a.value().isEmpty() ? field.getName() : a.value();
                }
                data.name = name;
                data.value = AsciiUtils.getFieldValue(field, obj, true);
                data.rawValue = AsciiUtils.getFieldValue(field, obj, false);

                if (!skipNullValues || data.rawValue != null) {
                    resultFields.put(field.getName(), data);
                }
            }
        }
        return resultFields;
    }

    private static AsciiMaker createAsciiMaker(String title) {
        var am = new AsciiMaker();
        am.addRule();
        am.add(null, "Command » " + title);
        am.addRule();
        return am;
    }

    private static String renderAsciiMaker(AsciiMaker am) {
        am.addRule();
        return am.render();
    }

    private static Object fixValue(Object value) {
        if (value == null) {
            value = "<null>";
        } else if (value instanceof String) {
            if (((String) value).isEmpty()) {
                value = "";
            }
        }
        return value;
    }

    protected static class FieldData {

        public String name;
        public Object value;
        public Object rawValue;
    }

    protected interface ISafeCommand {

        void execute(AsciiMaker am) throws Exception;
    }
}
