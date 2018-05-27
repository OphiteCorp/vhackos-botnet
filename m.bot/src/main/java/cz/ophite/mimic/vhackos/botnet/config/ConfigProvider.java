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
            config = loadConfigProperties(file);
        } else {
            LOG.debug("Configuration file '{}' has not been found and will be created", file.getName());
        }
        if (config == null) {
            config = createNewConfigFile(file);
        }
        LOG.info("Configuration from file '{}' was loaded", file.getName());
        return config;
    }

    /**
     * Získá název konfiguračního osuboru.
     */
    public String getConfigFileName() {
        return APPLICATION_CONFIG_FILE;
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
    private ApplicationConfig createNewConfigFile(File file) {
        try (var bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF-8"))) {
            LOG.info("Creating (configuration) properties file: {}", file.getName());

            bw.write("# The Ophite.botnet configuration file for mobile game vHackOS");
            bw.newLine();
            bw.write("# by mimic | © 2018");
            bw.newLine();
            bw.write("# ================================================================");
            bw.newLine();
            bw.newLine();

            var configData = prepareConfigData();

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
            var config = loadConfigProperties(file);
            LOG.debug("The configuration file '{}' was successfully created and loaded", file.getName());
            return config;

        } catch (Exception e) {
            LOG.error("An unexpected error occurred while creating a configuration file: " + file.getName(), e);
            throw new ConfigurationException("File '" + file.getName() + "' could not be created", e);
        }
    }

    private static Map<String, List<ConfigData>> prepareConfigData() throws Exception {
        var fields = ApplicationConfig.class.getDeclaredFields();
        var data = new LinkedHashMap<String, List<ConfigData>>();

        for (var f : fields) {
            if (f.isAnnotationPresent(ConfigValue.class)) {
                var cv = f.getAnnotation(ConfigValue.class);
                var d = new ConfigData();
                d.key = cv.value();
                d.value = replaceVariables(cv.defaultValue());
                d.category = cv.value().split("\\.")[0];

                if (!cv.comment().isEmpty()) {
                    var commentLines = cv.comment().split("\n");
                    d.comments = new ArrayList<>(commentLines.length);

                    for (var cl : commentLines) {
                        d.comments.add(replaceVariables(cl));
                    }
                } else {
                    d.comments = Collections.emptyList();
                }
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

    private static String replaceVariables(String text) throws Exception {
        var p = Pattern.compile("\\$\\{(.+?)\\}");
        var m = p.matcher(text);

        if (m.find()) {
            for (var i = 0; i < m.groupCount(); i++) {
                var group = m.group(i);
                var cmd = group.substring(2, group.length() - 1);
                var dotPos = cmd.lastIndexOf('.');

                if (dotPos > 0) {
                    var clazz = Class.forName(cmd.substring(0, dotPos));
                    var fieldName = cmd.substring(dotPos + 1);
                    var field = clazz.getDeclaredField(fieldName);
                    var value = field.get(null);

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

    private static final class ConfigData {

        private String category;
        private String key;
        private String value;
        private List<String> comments;
    }
}
