package cz.ophite.mimic.vhackos.botnet.utils;

import cz.ophite.mimic.vhackos.botnet.Application;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;

/**
 * Konstanty pro statický obsah.
 *
 * @author mimic
 */
public final class ResourceHelper {

    private static final Logger LOG = LoggerFactory.getLogger(ResourceHelper.class);

    public enum ResourceValue {

        FONT("/static/font.ttf"),
        SOUND_NEW_VERSION("/static/new_version.wav");

        private final String path;

        ResourceValue(String path) {
            this.path = path;
        }
    }

    public static InputStream getStream(ResourceValue resource) {
        return Application.class.getResourceAsStream(resource.path);
    }

    public static Font loadFont() {
        try (var is = getStream(ResourceValue.FONT)) {
            return Font.createFont(Font.TRUETYPE_FONT, is);

        } catch (IOException | FontFormatException e) {
            LOG.error("An error occurred while loading the font", e);
        }
        return null;
    }
}
