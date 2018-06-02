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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/**
 * Základní implementace všech služeb.
 *
 * @author mimic
 */
public abstract class Service implements IService {

    public static final String SERVICES_PACKAGE = "cz.ophite.mimic.vhackos.botnet.service";

    private static Map<String, IService> services;

    private final Botnet botnet;
    private long timeout;
    private long asyncCounter = -1;
    private boolean running;
    private boolean autoResetExecutor = true;
    private ServiceConfig serviceConfig;
    private ExecutorService executor;
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
    public final synchronized boolean start(ServiceConfig config) {
        if (running) {
            return false;
        }
        serviceConfig = config;

        if (config.isAsync() && !config.isFirstRunSync()) {
            if (!running && executor == null || executor.isTerminated()) {
                running = true;
                asyncCounter = 1;
                executor = createAndStartExecutor(config, 0);
                return true;
            }
        } else {
            running = true;
            new Run(config, false, 0).run();
            return true;
        }
        return false;
    }

    @Override
    public final synchronized boolean start() {
        var config = new ServiceConfig();
        config.setAsync(true);
        config.setFirstRunSync(false);

        return start(config);
    }

    @Override
    public final synchronized boolean stop() {
        if (executor != null) {
            running = false;

            if (!executor.isTerminated()) {
                log.info("Waiting for finish...");
                executor.shutdownNow();
                log.info("Stopped");
                onStopped();
                return true;
            } else {
                log.debug("Executor was already stopped");
            }
        } else if (running) {
            log.info("Stopped");
            running = false;
            onStopped();
            return true;
        } else {
            log.debug("Service is already stopped");
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

    protected final boolean isAutoResetExecutor() {
        return autoResetExecutor;
    }

    protected final void setAutoResetExecutor(boolean autoResetExecutor) {
        this.autoResetExecutor = autoResetExecutor;
    }

    protected abstract void initialize();

    protected abstract void execute() throws Exception;

    protected void onStopped() {
        // nic
    }

    protected final Logger getLog() {
        return log;
    }

    protected final long getTimeout() {
        return timeout;
    }

    protected final boolean isRunningAsync() {
        return (serviceConfig != null && serviceConfig.isAsync());
    }

    /**
     * Uspí vlákno na určitý čas.
     */
    protected boolean sleep(long millis) {
        try {
            log.debug("Forced timeout: {}ms", millis);
            Thread.sleep(millis);
            return true;

        } catch (InterruptedException e) {
            return false;
        }
    }

    private void validateTimeout() {
        if (timeout == 0) {
            throw new IllegalStateException("Timeout not set");
        }
    }

    private ExecutorService createAndStartExecutor(ServiceConfig config, long prevTimeout) {
        var executor = Executors.newFixedThreadPool(1, new ExecThreadFactory());
        executor.submit(new Run(config, true, prevTimeout));
        return executor;
    }

    /**
     * Uspí vlákno na čas z konfigurace.
     */
    protected boolean sleep() {
        return sleep(config.getSleepDelay());
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
        private final boolean async;
        private final long prevTimeout;

        private Run(ServiceConfig config, boolean async, long prevTimeout) {
            this.config = config;
            this.async = async;
            this.prevTimeout = prevTimeout;
        }

        @Override
        public void run() {
            // v případě async volání se při prvním průchodu vytvoří prodleva, protože původní sync volání již proběhlo
            if (async) {
                if (asyncCounter == 0) {
                    sleep(prevTimeout);
                }
            }
            do {
                // initializace služby
                try {
                    initialize();
                    validateTimeout();
                } catch (Exception e) {
                    log.error("There was an error initializing the service. The service will be terminated", e);
                    stop();
                    break;
                }
                log.info("Starting...");
                try {
                    execute();
                } catch (ConnectionException e) {
                    stop();
                    throw e;

                } catch (BotnetException e) {
                    log.error("An unexpected error occurred while processing the service", e);

                } catch (Exception e) {
                    log.error("There was a fatal error while processing the service. The service will be stopped", e);
                    stop();
                    throw new BotnetCoreException("There was a critical error when calling a service", e);
                }
                if (!config.isAsync()) {
                    log.info("Finished");
                } else {
                    log.debug("Finished {}. pass", asyncCounter);
                }
                // služba byla spuštěna sync a další volání již má být async
                if (executor == null && config.isAsync()) {
                    asyncCounter = 0;
                    executor = createAndStartExecutor(config, getTimeout());
                    break; // aktuální run() není async

                } else if (executor != null && autoResetExecutor && !executor.isTerminated()) {
                    if (!sleep(getTimeout())) {
                        running = false;
                    } else {
                        asyncCounter++;
                    }
                } else {
                    if (executor != null) {
                        stop();
                    }
                    running = false;
                }
            } while (running);
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
