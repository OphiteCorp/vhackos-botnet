package cz.ophite.mimic.vhackos.botnet.config;

import cz.ophite.mimic.vhackos.botnet.shared.injection.Inject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.*;
import java.util.regex.Pattern;

/**
 * Poskytuje konfiguraci aplikace. Umožňuje konfiguraci vytvořit i načíst.
 *
 * @author mimic
 */
@Inject
public final class ConfigProvider {

    private static final Logger LOG = LoggerFactory.getLogger(ConfigProvider.class);

    private static final String APPLICATION_CONFIG_FILE = "botnet.properties";

    /**
     * Získá konfigurační soubor aplikace.
     */
    public synchronized ApplicationConfig getAppConfig() {
        var file = new File(APPLICATION_CONFIG_FILE);
        ApplicationConfig config = null;

        if (file.exists() && file.isFile()) {
            LOG.debug("Configuration file '{}' was found and will be loaded", file.getName());
            checkAndMergeConfig(file);
            config = loadConfigProperties(file);
        } else {
            LOG.debug("Configuration file '{}' has not been found and will be created", file.getName());
        }
        if (config == null) {
            createNewConfigFile(file);
            config = loadConfigProperties(file);
            LOG.debug("The configuration file '{}' was successfully created and loaded", file.getName());
        }
        LOG.info("Configuration from file '{}' was loaded", file.getName());
        return config;
    }

    /**
     * Načte konfiguraci ze souboru.
     */
    private ApplicationConfig loadConfigProperties(File file) {
        try (var fis = new FileInputStream(file)) {
            var prop = new Properties();
            prop.load(fis);
            LOG.debug("Properties file '{}' was loaded", file.getName());
            var config = mapToConfiguration(prop);
            LOG.debug("{} file was successfully loaded", ApplicationConfig.class.getSimpleName());
            return config;

        } catch (Exception e) {
            LOG.error("An error occurred while loading the configuration file: " + file.getName(), e);
            return null;
        }
    }

    /**
     * Vytvoří nový konfigurační soubor.
     */
    private void createNewConfigFile(File file, Map<String, List<ConfigData>> configData) {
        try (var bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF-8"))) {
            LOG.info("Creating (configuration) properties file: {}", file.getName());

            bw.write("# The Ophite.botnet configuration file for mobile game vHackOS");
            bw.newLine();
            bw.write("# by mimic | © 2018");
            bw.newLine();
            bw.write("# ================================================================");
            bw.newLine();
            bw.newLine();

            for (var entry : configData.entrySet()) {
                bw.write(String.format("[%s]", entry.getKey()));
                bw.newLine();
                bw.newLine();

                for (var cd : entry.getValue()) {
                    for (var c : cd.comments) {
                        bw.write("# " + c);
                        bw.newLine();
                    }
                    bw.write(cd.key + " = " + cd.value);
                    bw.newLine();
                    bw.newLine();
                    LOG.debug("Write value: {} = {}", cd.key, cd.value);
                }
            }
        } catch (Exception e) {
            LOG.error("An unexpected error occurred while creating a configuration file: " + file.getName(), e);
            throw new ConfigurationException("File '" + file.getName() + "' could not be created", e);
        }
    }

    /**
     * Vytvoří nový konfigurační soubor.
     */
    private void createNewConfigFile(File file) {
        var configData = prepareConfigData();
        createNewConfigFile(file, configData);
    }

    /**
     * Zkontroluje konfigurační soubor a pokud v něm chybí nějaké klíče, tak je doplní.
     */
    private void checkAndMergeConfig(File file) {
        try {
            LOG.debug("Start checking the configuration file and eventually merge");
            var missingKeys = new ArrayList<ConfigData>();
            var current = prepareConfigData();
            Properties prop;

            try (var fis = new FileInputStream(file)) {
                prop = new Properties();
                prop.load(fis);

                for (var entry : prop.entrySet()) {
                    if (isCategory(entry.getKey().toString())) {
                        prop.remove(entry.getKey());
                    }
                }
                var names = prop.stringPropertyNames();

                for (var data : current.values()) {
                    for (var cd : data) {
                        var found = false;

                        for (var name : names) {
                            if (name.equals(cd.key)) {
                                found = true;
                                break;
                            }
                        }
                        if (!found) {
                            missingKeys.add(cd);
                        }
                    }
                }
            }
            if (!missingKeys.isEmpty()) {
                LOG.info("{} new configurations have been found. Starting merge configuration...", missingKeys.size());

                for (var data : current.values()) {
                    for (var cd : data) {
                        for (var entry : prop.entrySet()) {
                            if (cd.key.equals(entry.getKey())) {
                                cd.value = entry.getValue().toString();
                                break;
                            }
                        }
                    }
                }
                createNewConfigFile(file, current);
                LOG.info("Merge configuration file complete");
            }
        } catch (Exception e) {
            throw new ConfigurationException("An error occurred while merging the configuration file", e);
        }
    }

    private static Map<String, List<ConfigData>> prepareConfigData() {
        var fields = ApplicationConfig.class.getDeclaredFields();
        var data = new LinkedHashMap<String, List<ConfigData>>();

        for (var f : fields) {
            if (f.isAnnotationPresent(ConfigValue.class)) {
                var cv = f.getAnnotation(ConfigValue.class);
                var d = new ConfigData();
                d.key = cv.value();
                d.value = replaceVariables(cv.defaultValue());
                d.category = cv.value().split("\\.")[0];
                d.comments = mapToComments(cv.comment());

                List<ConfigData> list;
                if (data.containsKey(d.category)) {
                    list = data.get(d.category);
                } else {
                    list = new ArrayList<>();
                    data.put(d.category, list);
                }
                list.add(d);
            }
        }
        return data;
    }

    /**
     * Přemapuje properties soubor do konfigurace.
     */
    private ApplicationConfig mapToConfiguration(Properties prop) {
        var config = new ApplicationConfig();
        var fields = config.getClass().getDeclaredFields();

        try {
            for (var f : fields) {
                if (f.isAnnotationPresent(ConfigValue.class)) {
                    var cv = f.getAnnotation(ConfigValue.class);
                    var value = prop.getProperty(cv.value(), cv.defaultValue());

                    if (StringUtils.isEmpty(value) && !cv.canBeEmpty()) {
                        value = cv.defaultValue();
                    }
                    f.setAccessible(true);
                    f.set(config, value);
                }
            }
        } catch (Exception e) {
            LOG.error("Error while mapping file properties to configuration", e);
            throw new ConfigurationException("An error occurred while converting the configuration file", e);
        }
        return config;
    }

    /**
     * Přemapuje řádek s komentářem na víceřádkový komentář.
     */
    private static List<String> mapToComments(String comment) {
        if (!comment.isEmpty()) {
            var commentLines = comment.split("\n");
            var list = new ArrayList<String>(commentLines.length);

            for (var cl : commentLines) {
                list.add(replaceVariables(cl));
            }
            return list;
        } else {
            return Collections.emptyList();
        }
    }

    private static String replaceVariables(String text) {
        var p = Pattern.compile("\\$\\{(.+?)\\}");
        var m = p.matcher(text);

        if (m.find()) {
            for (var i = 0; i < m.groupCount(); i++) {
                var group = m.group(i);
                var cmd = group.substring(2, group.length() - 1);
                var dotPos = cmd.lastIndexOf('.');

                if (dotPos > 0) {
                    Object value;
                    try {
                        var clazz = Class.forName(cmd.substring(0, dotPos));
                        var fieldName = cmd.substring(dotPos + 1);
                        var field = clazz.getDeclaredField(fieldName);
                        value = field.get(null);

                    } catch (Exception e) {
                        throw new ConfigurationException("There was an error getting a configuration variable. Command is: " + cmd, e);
                    }
                    if (value != null) {
                        if (value instanceof Collection) {
                            var c = (Collection) value;
                            var sb = new StringBuilder();
                            var n = 0;

                            for (Object obj : c) {
                                sb.append(obj);
                                if (n++ < c.size() - 1) {
                                    sb.append(", ");
                                }
                            }
                            value = sb.toString();
                        }
                    }
                    var regex = escapeRegex(group);
                    text = text.replaceAll(regex, value.toString());
                    System.out.println();
                }
            }
        }
        return text;
    }

    private static String escapeRegex(String regex) {
        regex = regex.replaceAll("\\$", "\\\\\\$");
        regex = regex.replaceAll("\\{", "\\\\\\{");
        regex = regex.replaceAll("\\}", "\\\\\\}");
        return regex;
    }

    private static boolean isCategory(String key) {
        return key.matches("\\[(.+?)\\]");
    }

    private static final class ConfigData {

        private String category;
        private String key;
        private String value;
        private List<String> comments;
    }
}
