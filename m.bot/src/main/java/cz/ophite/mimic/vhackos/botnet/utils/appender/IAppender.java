package cz.ophite.mimic.vhackos.botnet.utils.appender;

import org.apache.logging.log4j.core.LogEvent;

/**
 * Rozhraní pro logovací appender.
 *
 * @author mimic
 */
public interface IAppender {

    void append(LogEvent event, String message);
}
