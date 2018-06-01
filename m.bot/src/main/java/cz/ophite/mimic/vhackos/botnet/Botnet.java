package cz.ophite.mimic.vhackos.botnet;

import cz.ophite.mimic.vhackos.botnet.api.IBotnet;
import cz.ophite.mimic.vhackos.botnet.api.IBotnetConfig;
import cz.ophite.mimic.vhackos.botnet.api.dto.ConnectionData;
import cz.ophite.mimic.vhackos.botnet.api.net.response.LoginResponse;
import cz.ophite.mimic.vhackos.botnet.config.ApplicationConfig;
import cz.ophite.mimic.vhackos.botnet.dto.BotnetSharedData;
import cz.ophite.mimic.vhackos.botnet.dto.CacheData;
import cz.ophite.mimic.vhackos.botnet.exception.MissingLoginCredentialException;
import cz.ophite.mimic.vhackos.botnet.service.base.IService;
import cz.ophite.mimic.vhackos.botnet.service.base.Service;
import cz.ophite.mimic.vhackos.botnet.service.base.ServiceConfig;
import cz.ophite.mimic.vhackos.botnet.shared.injection.Autowired;
import cz.ophite.mimic.vhackos.botnet.shared.json.Json;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Locale;

/**
 * Hlavní třída Botnet.
 *
 * @author mimic
 */
public final class Botnet implements IBotnet {

    private static final Logger LOG = LoggerFactory.getLogger(Botnet.class);

    @Autowired
    private ApplicationConfig config;

    @Autowired
    private ConnectionData connectionData;

    private volatile BotnetSharedData sharedData;

    public Botnet() {
        sharedData = new BotnetSharedData();
    }

    @Override
    public IBotnetConfig getConfig() {
        return config;
    }

    @Override
    public ConnectionData getConnectionData() {
        return connectionData;
    }

    @Override
    public void reloginCallback(LoginResponse loginData) {
        createCacheData();
    }

    /**
     * Spustí botnet.
     */
    void start() {
        if (!config.hasValidCredentials()) {
            throw new MissingLoginCredentialException();
        }
        // připraví connection data
        connectionData.setLang(Locale.getDefault().getLanguage());
        connectionData.setAccessToken(config.getFixedAccessToken());
        connectionData.setUid(config.getFixedUserUid());
        connectionData.setUserName(config.getUserName());

        // načte cache z předchozí session
        var cache = loadCacheData();
        if (cache != null && getConfig().getUserName().equalsIgnoreCase(cache.getConnectionData().getUserName())) {
            connectionData.set(cache.getConnectionData());
        }
        // zkontroluje, případně přihlásí uživatele s novým tokenem
        LOG.info("Getting user '{}' information. Please wait...", config.getUserName());
        var serviceConfig = new ServiceConfig();
        serviceConfig.setAsync(false);
        serviceConfig.setFirstRunSync(true);
        Service.getServices().get(IService.SERVICE_UPDATE).start(serviceConfig);

        // spustí služby
        initializeServices();
    }

    public BotnetSharedData getSharedData() {
        return sharedData;
    }

    private void initializeServices() {
        var services = Service.getServices();

        if (config.isMinerEnable()) {
            services.get(IService.SERVICE_MINER).start();
        }
        if (config.isMalwareEnable()) {
            services.get(IService.SERVICE_MALWARE).start();
        }
        if (config.isServerEnable()) {
            services.get(IService.SERVICE_SERVER).start();
        }
        if (config.isStoreEnable()) {
            services.get(IService.SERVICE_STORE).start();
        }
        if (config.isBoosterEnable()) {
            services.get(IService.SERVICE_BOOSTER).start();
        }
        if (config.isMissionEnable()) {
            services.get(IService.SERVICE_MISSION).start();
        }
        if (config.isNetworkEnable()) {
            services.get(IService.SERVICE_NETWORK).start();
        }
        if (config.isNetworkScanEnable()) {
            services.get(IService.SERVICE_NETWORK_SCAN).start();
        }
    }

    /**
     * Ukončí botnet. Volá se automaticky při ukončení aplikace.
     */
    void shutdown() {
        LOG.debug("Shutdown botnet called");
        var services = Service.getServices();
        for (var service : services.values()) {
            service.stop();
        }
        createCacheData();
    }

    private CacheData loadCacheData() {
        var cacheData = Json.toObject(new File(CacheData.FILE_NAME), CacheData.class);

        if (cacheData != null) {
            LOG.info("Cache was retrieved from file: {}", CacheData.FILE_NAME);
        }
        return cacheData;
    }

    private void createCacheData() {
        var data = new CacheData();
        data.setConnectionData(connectionData);

        if (Json.toFile(CacheData.FILE_NAME, data) != null) {
            LOG.info("The cache has been saved to a file: {}", CacheData.FILE_NAME);
        }
    }
}
