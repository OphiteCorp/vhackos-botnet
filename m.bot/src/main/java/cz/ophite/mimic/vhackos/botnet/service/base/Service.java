package cz.ophite.mimic.vhackos.botnet.service.base;

import cz.ophite.mimic.vhackos.botnet.Botnet;
import cz.ophite.mimic.vhackos.botnet.api.exception.BotnetCoreException;
import cz.ophite.mimic.vhackos.botnet.api.exception.BotnetException;
import cz.ophite.mimic.vhackos.botnet.api.exception.ConnectionException;
import cz.ophite.mimic.vhackos.botnet.api.module.CommonModule;
import cz.ophite.mimic.vhackos.botnet.config.ApplicationConfig;
import cz.ophite.mimic.vhackos.botnet.dto.BotnetSharedData;
import cz.ophite.mimic.vhackos.botnet.shared.injection.Autowired;
import cz.ophite.mimic.vhackos.botnet.shared.injection.InjectionContext;
import org.reflections.Reflections;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/**
 * Základní implementace všech služeb.
 *
 * @author mimic
 */
public abstract class Service implements IService {

    public static final String SERVICES_PACKAGE = "cz.ophite.mimic.vhackos.botnet.service";
    private static final long DEFAULT_INIT_DELAY = 1000;

    private static Map<String, IService> services;

    private final Botnet botnet;
    private long timeout;
    private long initDelay = DEFAULT_INIT_DELAY;
    private boolean running;
    private boolean autoResetExecutor = true;
    private ScheduledExecutorService executor;
    private Logger log;

    @Autowired
    private ApplicationConfig config;

    @Autowired
    private CommonModule commonModule;

    protected Service(Botnet botnet) {
        this.botnet = botnet;
        log = LoggerFactory.getLogger(getClass());
    }

    /**
     * Získá všechny dostupné služby.
     */
    public static synchronized Map<String, IService> getServices() {
        if (services == null) {
            var ref = new Reflections(SERVICES_PACKAGE, new TypeAnnotationsScanner());
            var classes = ref.getTypesAnnotatedWith(EndpointService.class, true);
            var ctx = InjectionContext.getInstance();
            services = new TreeMap<>();

            for (var clazz : classes) {
                var a = clazz.getAnnotation(EndpointService.class);
                services.put(a.value(), (IService) ctx.get(clazz));
            }
        }
        return services;
    }

    @Override
    public final boolean start(ServiceConfig config) {
        initialize();

        if (config.isAsync() && !config.isFirstRunSync()) {
            if (executor == null || executor.isShutdown()) {
                validateTimeout();
                executor = Executors.newScheduledThreadPool(1, new ExecThreadFactory());
                executor.scheduleAtFixedRate(new Run(config), initDelay, timeout, TimeUnit.MILLISECONDS);
                running = true;
                return true;
            }
        } else {
            new Run(config).run();
            return true;
        }
        return false;
    }

    @Override
    public final boolean start() {
        var config = new ServiceConfig();
        config.setAsync(true);
        config.setFirstRunSync(false);

        return start(config);
    }

    @Override
    public final boolean stop() {
        if (executor != null) {
            if (!executor.isShutdown() && !executor.isTerminated()) {
                log.info("Waiting for finish...");
                executor.shutdownNow();
                log.info("Stopped");
                running = false;
                return true;
            } else {
                log.debug("Executor was already stopped");
            }
        }
        return false;
    }

    @Override
    public final boolean isRunning() {
        return running;
    }

    protected final BotnetSharedData getShared() {
        return botnet.getSharedData();
    }

    protected final ApplicationConfig getConfig() {
        return config;
    }

    protected final CommonModule getCommonModule() {
        return commonModule;
    }

    protected final void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    protected final void setInitDelay(long initDelay) {
        this.initDelay = initDelay;
    }

    protected final boolean isAutoResetExecutor() {
        return autoResetExecutor;
    }

    protected final void setAutoResetExecutor(boolean autoResetExecutor) {
        this.autoResetExecutor = autoResetExecutor;
    }

    protected abstract void initialize();

    protected abstract void execute() throws Exception;

    protected final Logger getLog() {
        return log;
    }

    protected final long getTimeout() {
        return timeout;
    }

    /**
     * Uspí vlákno na určitý čas.
     */
    protected void sleep(long millis) {
        try {
            log.debug("Forced timeout: {}ms", millis);
            Thread.sleep(millis);

        } catch (InterruptedException e) {
            log.error("There was an error while sleeping thread", e);
        }
    }

    private void validateTimeout() {
        if (timeout == 0) {
            throw new IllegalStateException("Timeout not set");
        }
    }

    /**
     * Uspí vlákno na čas z konfigurace.
     */
    protected void sleep() {
        sleep(config.getSleepDelay());
    }

    /**
     * Má uživatel dostatek netcoins?
     */
    protected final boolean hasEnoughNetcoins() {
        var netcoins = getShared().getUpdateResponse().getNetCoins();
        return hasEnoughNetcoins(netcoins);
    }

    /**
     * Má uživatel dostatek netcoins?
     */
    protected final boolean hasEnoughNetcoins(int netcoins) {
        return (netcoins > getConfig().getSafeNetcoins());
    }

    /**
     * Samotný process.
     */
    private class Run implements Runnable {

        private final ServiceConfig config;

        private Run(ServiceConfig config) {
            this.config = config;
        }

        @Override
        public void run() {
            log.info("Starting...");
            try {
                execute();
            } catch (ConnectionException e) {
                throw e;
            } catch (BotnetException e) {
                log.error("An unexpected error occurred while processing the service", e);
            } catch (Exception e) {
                throw new BotnetCoreException("There was a critical error when calling a service", e);
            }
            log.debug("Finished");

            if (executor == null && config.isFirstRunSync()) {
                validateTimeout();
                executor = Executors.newScheduledThreadPool(1, new ExecThreadFactory());
                executor.scheduleAtFixedRate(this, timeout, timeout, TimeUnit.MILLISECONDS);
                running = true;

            } else if (autoResetExecutor && executor != null && !executor.isShutdown()) {
                executor.shutdownNow();
                initialize();
                executor = Executors.newScheduledThreadPool(1, new ExecThreadFactory());
                executor.scheduleAtFixedRate(this, timeout, timeout, TimeUnit.MILLISECONDS);
            }
        }
    }

    /**
     * Implementace vlastní factory, pro vytvoření vlákna pro executor.
     */
    private static class ExecThreadFactory implements ThreadFactory {

        @Override
        public Thread newThread(Runnable r) {
            var t = new Thread(r, "Service - " + getClass().getSimpleName());
            t.setPriority(Thread.MIN_PRIORITY);
            t.setDaemon(false);
            return t;
        }
    }
}
