package cz.ophite.mimic.vhackos.botnet.utils.appender;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.config.AppenderControl;
import org.apache.logging.log4j.core.config.AppenderControlArraySet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Hacknutý appender, který zachytává logy jiného appenderu.
 *
 * @author mimic
 */
public final class HackedAppender extends AbstractAppender {

    private static final Logger LOG = LoggerFactory.getLogger(HackedAppender.class);
    private static HackedAppender instance;

    private static final String NAME = "hacked_appender";
    private static final String SOURCE_APPENDER = "console_appender";

    private final List<IAppender> listeners;

    private HackedAppender(final Layout<? extends Serializable> layout) {
        super(NAME, null, layout);
        listeners = new ArrayList<>();
    }

    public void addListener(IAppender appender) {
        listeners.add(appender);
    }

    public static synchronized HackedAppender getInstance() {
        if (instance == null) {
            instance = injectAppender();
        }
        return instance;
    }

    @Override
    public void append(final LogEvent event) {
        var message = new String(getLayout().toByteArray(event));
        for (var listener : listeners) {
            listener.append(event, message);
        }
    }

    private static HackedAppender injectAppender() {
        try {
            var fLogger = LOG.getClass().getDeclaredField("logger");
            fLogger.setAccessible(true);
            var logger = fLogger.get(LOG);

            var fPrivateConfig = logger.getClass().getDeclaredField("privateConfig");
            fPrivateConfig.setAccessible(true);
            var privateConfig = fPrivateConfig.get(logger);

            var fLoggerConfig = privateConfig.getClass().getDeclaredField("loggerConfig");
            fLoggerConfig.setAccessible(true);
            var loggerConfig = fLoggerConfig.get(privateConfig);

            var fLevel = loggerConfig.getClass().getDeclaredField("level");
            fLevel.setAccessible(true);
            var level = (Level) fLevel.get(loggerConfig);

            var fAppenders = loggerConfig.getClass().getDeclaredField("appenders");
            fAppenders.setAccessible(true);
            var appenders = (AppenderControlArraySet) fAppenders.get(loggerConfig);

            var consoleAppender = appenders.asMap().get(SOURCE_APPENDER);
            var hackedAppender = new HackedAppender(consoleAppender.getLayout());
            var control = new AppenderControl(hackedAppender, level, null);
            appenders.add(control);
            return hackedAppender;

        } catch (Exception e) {
            LOG.error("There was an error in injecting hacked appender", e);
        }
        return null;
    }
}
