package cz.ophite.mimic.vhackos.botnet.command;

import cz.ophite.mimic.vhackos.botnet.Botnet;
import cz.ophite.mimic.vhackos.botnet.command.base.BaseCommand;
import cz.ophite.mimic.vhackos.botnet.service.base.IService;
import cz.ophite.mimic.vhackos.botnet.service.base.Service;
import cz.ophite.mimic.vhackos.botnet.service.base.ServiceConfig;
import cz.ophite.mimic.vhackos.botnet.shared.command.Command;
import cz.ophite.mimic.vhackos.botnet.shared.command.CommandParam;
import cz.ophite.mimic.vhackos.botnet.shared.injection.Inject;
import cz.ophite.mimic.vhackos.botnet.shared.injection.InjectionContext;
import cz.ophite.mimic.vhackos.botnet.shared.utils.SharedUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * Správa služeb přes příkazový řádek.
 *
 * @author mimic
 */
@Inject
public final class ServiceCommand extends BaseCommand {

    protected ServiceCommand(Botnet botnet) {
        super(botnet);
    }

    /**
     * Vypíše dostupné služby.
     */
    @Command(value = "services", comment = "Lists available services")
    private String getServices() {
        return execute("Service | list", am -> {
            var services = Service.getServices();

            for (var entry : services.entrySet()) {
                var running = SharedUtils.convertToBoolean(entry.getValue().isRunning());
                var desc = entry.getValue().getDescription();
                put(am, entry.getKey(), String.format(" %s | %s", StringUtils.rightPad(running, 3), desc));
            }
        });
    }

    /**
     * Spustí službu.
     */
    @Command(value = "service start", comment = "Starts the service")
    private String startService(@CommandParam("name") String serviceName) {
        return execute("Service | start -> " + serviceName, am -> {
            var service = getServiceByName(serviceName);
            Boolean started = null;

            if (service != null) {
                started = service.start();
            }
            if (started == null) {
                am.add("Error", "Service was not found");
            } else if (started) {
                am.add("Success", "Service has been started");
            } else {
                am.add("Error", "Service is already running");
            }
        });
    }

    /**
     * Spustí službu.
     */
    @Command(value = "service start now", comment = "Start the service one-time")
    private String startServiceNew(@CommandParam("name") String serviceName) {
        return execute("Service | start now -> " + serviceName, am -> {
            var service = getServiceByName(serviceName);

            if (service != null) {
                var c = service.getClass().getDeclaredConstructor(Botnet.class);
                c.setAccessible(true);
                var instance = c.newInstance(getBotnet());
                InjectionContext.lazyInit(instance);
                var serviceConfig = new ServiceConfig();
                serviceConfig.setAsync(false);
                serviceConfig.setFirstRunSync(true);
                instance.start(serviceConfig);
                am.add("Success", "The service has been completed and terminated");
            } else {
                am.add("Error", "Service was not found");
            }
        });
    }

    /**
     * Zastaví službu.
     */
    @Command(value = "service stop", comment = "Stops the service")
    private String stopService(@CommandParam("name") String serviceName) {
        return execute("Service | stop -> " + serviceName, am -> {
            var service = getServiceByName(serviceName);
            Boolean stopped = null;

            if (service != null) {
                stopped = service.stop();
            }
            if (stopped == null) {
                am.add("Error", "Service was not found");
            } else if (stopped) {
                am.add("Success", "Service has been stopped");
            } else {
                am.add("Error", "Service is not running");
            }
        });
    }

    private static IService getServiceByName(String serviceName) {
        var services = Service.getServices();
        serviceName = serviceName.toUpperCase();

        if (!services.isEmpty()) {
            for (var service : services.entrySet()) {
                if (service.getKey().equalsIgnoreCase(serviceName)) {
                    return service.getValue();
                }
            }
        }
        return null;
    }
}
