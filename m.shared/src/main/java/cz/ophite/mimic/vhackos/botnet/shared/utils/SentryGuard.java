package cz.ophite.mimic.vhackos.botnet.shared.utils;

import com.sun.security.auth.module.NTSystem;
import cz.ophite.mimic.vhackos.botnet.shared.SharedConst;
import io.sentry.SentryClient;
import io.sentry.SentryClientFactory;
import io.sentry.event.Event;
import io.sentry.event.EventBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Správa sentry pro online logování.
 *
 * @author mimic
 */
public final class SentryGuard {

    private static final Logger LOG = LoggerFactory.getLogger(SentryGuard.class);

    private static final String DSN = "https://486b32e2827a421ebd232e7e683b6eb4@sentry.io/1221745";
    private static final String TAG_BOTNET_USER = "botnet_user";
    private static final boolean ENABLE = !SharedConst.DEBUG;

    private static SentryClient sentry;

    /**
     * Vytvoření sentry.
     */
    public static void init(String botnetVersion) {
        if (ENABLE) {
            sentry = SentryClientFactory.sentryClient(DSN);

            var ntSystem = new NTSystem();
            sentry.addTag("server_user", ntSystem.getName());
            sentry.addTag("botnet_version", botnetVersion);
            sentry.addTag("server_os", System.getProperty("os.name"));

            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                try {
                    LOG.info("Closing sentry...");
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    // nic
                } finally {
                    sentry.clearContext();
                    sentry.closeConnection();
                }
            }));
        }
    }

    /**
     * Přidá tag uživatele botnetu.
     */
    public static void setBotnetUser(String user) {
        if (ENABLE) {
            if (user != null && !sentry.getTags().containsKey(TAG_BOTNET_USER)) {
                sentry.addTag(TAG_BOTNET_USER, user);
            }
        }
    }

    /**
     * Odešle zprávu.
     */
    public static void log(String message) {
        if (ENABLE) {
            LOG.debug("Sending a message to the sentry: {}", message);
            sentry.sendMessage(message);
        }
    }

    /**
     * Odešle warning zprávu.
     */
    public static void logWarning(String message) {
        if (ENABLE) {
            LOG.debug("Sending a warning message to the sentry: {}", message);
            sentry.sendEvent(new EventBuilder().withLevel(Event.Level.WARNING).withMessage(message).build());
        }
    }

    /**
     * Odešle debug zprávu.
     */
    public static void logDebug(String message) {
        if (ENABLE) {
            LOG.debug("Sending a warning message to the sentry: {}", message);
            sentry.sendEvent(new EventBuilder().withLevel(Event.Level.DEBUG).withMessage(message).build());
        }
    }

    /**
     * Odešle error zprávu.
     */
    public static void logError(String message, Object extra) {
        if (ENABLE) {
            LOG.debug("Sending an exception to the sentry: {}", message);
            sentry.sendEvent(new EventBuilder().withLevel(Event.Level.ERROR).withMessage(message)
                    .withExtra("userData", extra).build());
        }
    }

    /**
     * Odešle error zprávu.
     */
    public static void logError(String message) {
        if (ENABLE) {
            LOG.debug("Sending an exception to the sentry: {}", message);
            sentry.sendEvent(new EventBuilder().withLevel(Event.Level.ERROR).withMessage(message).build());
        }
    }

    /**
     * Odešle vyjímku.
     */
    public static void log(Exception ex) {
        if (ENABLE) {
            LOG.debug("Sending an exception to the sentry: {}", ex.getMessage());
            sentry.sendException(ex);
        }
    }
}
