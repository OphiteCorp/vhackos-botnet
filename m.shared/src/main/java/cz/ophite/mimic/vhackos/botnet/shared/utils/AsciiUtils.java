package cz.ophite.mimic.vhackos.botnet.shared.utils;

import cz.ophite.mimic.vhackos.botnet.shared.ascii.AsciiMaker;
import cz.ophite.mimic.vhackos.botnet.shared.ascii.AsciiRow;
import cz.ophite.mimic.vhackos.botnet.shared.ascii.IAsciiConverter;
import cz.ophite.mimic.vhackos.botnet.shared.injection.InjectionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * Pomocné metody kolem ASCII.
 *
 * @author mimic
 */
public final class AsciiUtils {

    private static final Logger LOG = LoggerFactory.getLogger(AsciiUtils.class);

    /**
     * Získá hodnotu z fieldu. Pokud field bude mít nastavený ASCII converter, tak ho použije taky.
     */
    public static <T> T getFieldValue(Field field, Object instance, boolean applyConverter) {
        IAsciiConverter converter = null;

        if (field.isAnnotationPresent(AsciiRow.class)) {
            var a = field.getDeclaredAnnotation(AsciiRow.class);

            try {
                var converterClass = Class.forName(a.converter().getName());
                if (!converterClass.isInterface()) {
                    converter = InjectionContext.getInstance().get(converterClass.getName());
                }
            } catch (ClassNotFoundException e) {
                throw new IllegalStateException("The conveter class '" + a.converter() + "' was not found", e);
            }
        }
        try {
            field.setAccessible(true);
            var value = field.get(instance);

            if (applyConverter && converter != null) {
                value = converter.convert(value);
            }
            return (T) value;

        } catch (IllegalAccessException e) {
            throw new IllegalStateException("Could not get the value of: " + field.getName(), e);
        }
    }

    /**
     * Získá hodnotu z fieldu. Pokud field bude mít nastavený ASCII converter, tak ho použije taky.
     */
    public static <T> T getFieldValue(String fieldName, Object instance, boolean applyConverter) {
        try {
            var field = instance.getClass().getDeclaredField(fieldName);
            return getFieldValue(field, instance, applyConverter);

        } catch (NoSuchFieldException e) {
            throw new IllegalStateException("Could not get field '" + fieldName + "' from class: " + instance.getClass()
                    .getSimpleName());
        }
    }

    /**
     * Převede všechny fieldy v instanci, které nejsou označeny jako transient do ASCII tabulky.
     */
    public static String toFieldsAsciiTable(String header, Object instance, boolean onlyAsciiRows) {
        var am = new AsciiMaker();
        am.addRule();
        am.add(null, header);
        am.addRule();

        if (instance == null) {
            am.add("Input instance is NULL");
            am.addRule();
            return am.render();
        }
        am.add("Key", "Value");
        am.addRule();

        var fields = instance.getClass().getDeclaredFields();
        for (var field : fields) {
            if (Modifier.isStatic(field.getModifiers())) {
                continue;
            }
            if ((field.isAnnotationPresent(AsciiRow.class) && onlyAsciiRows) || !onlyAsciiRows) {
                var name = field.getName();

                if (field.isAnnotationPresent(AsciiRow.class)) {
                    var a = field.getDeclaredAnnotation(AsciiRow.class);
                    name = a.value().isEmpty() ? field.getName() : a.value();
                }
                try {
                    field.setAccessible(true);
                    var value = field.get(instance);
                    value = (value != null) ? value.toString() : "<NULL>";
                    am.add(name, value);

                } catch (IllegalAccessException e) {
                    throw new RuntimeException("There was an unexpected error in getting value from field: " + field
                            .getName(), e);
                }
            }
        }
        am.addRule();
        return am.render();
    }
}
