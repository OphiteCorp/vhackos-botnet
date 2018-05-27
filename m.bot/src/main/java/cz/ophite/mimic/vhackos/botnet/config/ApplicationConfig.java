package cz.ophite.mimic.vhackos.botnet.config;

import cz.ophite.mimic.vhackos.botnet.api.IBotnetConfig;
import cz.ophite.mimic.vhackos.botnet.api.dto.ProxyData;
import cz.ophite.mimic.vhackos.botnet.shared.dto.AppStoreType;
import cz.ophite.mimic.vhackos.botnet.shared.injection.Inject;
import cz.ophite.mimic.vhackos.botnet.shared.utils.SharedUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

/**
 * Konfigurace aplikace.
 *
 * @author mimic
 */
@Inject
public final class ApplicationConfig implements IBotnetConfig {

    private static final String DEFAULT_USERNAME = "YOUR_USERNAME";
    private static final String DEFAULT_PASSWORD = "YOUR_PASSWORD";

    // jméno a heslo

    @ConfigValue(value = "game.userName", defaultValue = DEFAULT_USERNAME,
                 comment = "The login name of the user who is logged in to the game")
    private String userName;

    @ConfigValue(value = "game.password", defaultValue = DEFAULT_PASSWORD,
                 comment = "The password with which you log in to the game")
    private String password;

    // fixní přihlašovací token a UID - vhodné v případě, že nechceme vygenerovat nový token, ale použít stavající
    // (např. z mobilu)

    @ConfigValue(value = "game.fixed.access.token", comment = "If set, this access token will be used",
                 canBeEmpty = true)
    private String fixedAccessToken;

    @ConfigValue(value = "game.fixed.user.uid", comment = "If set, this user UID will be used", canBeEmpty = true)
    private String fixedUserUid;

    // zpráva ponechaná v logu cílového systému

    @ConfigValue(value = "game.message.left.in.log", defaultValue = "I did not take the money!\\nby Ophite.botnet",
                 comment = "The message will be left in the remote system log")
    private String messageLog;

    @ConfigValue(value = "game.safe.netcoins", defaultValue = "5000",
                 comment = "Minimum number of netcoins for which something to buy")
    private String safeNetcoins;

    @ConfigValue(value = "game.safe.boosters", defaultValue = "20",
                 comment = "Minimum number of boosters that will no longer be used")
    private String safeBoosters;

    // maximální počet pokusů na odeslání požadavku na vHack server

    @ConfigValue(value = "sys.max.request.attempts",
                 comment = "The maximum number of attempts to send the request to the server", defaultValue = "3")
    private String maxRequestAttempts;

    @ConfigValue(value = "sys.sleep.delay", comment = "Delay before each server request in milliseconds",
                 defaultValue = "(500+(Math.floor(Math.random()*2001)+500))")
    private String sleepDelay;

    @ConfigValue(value = "sys.proxy.enable", comment = "Enables the use of a proxy server", defaultValue = "False")
    private String proxyEnable;

    @ConfigValue(value = "sys.proxy.host",
                 comment = "Proxy server IP address. Currently supports only HTTP connections",
                 defaultValue = "136.243.63.53")
    private String proxyHost;

    @ConfigValue(value = "sys.proxy.port", comment = "Proxy server port", defaultValue = "3128")
    private String proxyPort;

    // služba - update

    @ConfigValue(value = "service.update.timeout", comment = "Delay between repeated executing in milliseconds",
                 defaultValue = "(5*60+(Math.floor(Math.random()*111)+10))*1e3")
    private String sUpdateTimeout;

    // služba - miner

    @ConfigValue(value = "service.miner.enable", comment = "Enable netcoins mining service", defaultValue = "True")
    private String sMinerEnable;

    @ConfigValue(value = "service.miner.timeout", comment = "Delay between repeated executing in milliseconds",
                 defaultValue = "(60*60+(Math.floor(Math.random()*51)+10))*1e3")
    private String sMinerTimeout;

    // služba - malware

    @ConfigValue(value = "service.malware.enable", comment = "Enable malware production service", defaultValue = "True")
    private String sMalwareEnable;

    @ConfigValue(value = "service.malware.timeout", comment = "Delay between repeated executing in milliseconds",
                 defaultValue = "(10*60+(Math.floor(Math.random()*51)+10))*1e3")
    private String sMalwareTimeout;

    // služba - server

    @ConfigValue(value = "service.server.enable", comment = "Enable server control", defaultValue = "True")
    private String sServerEnable;

    @ConfigValue(value = "service.server.timeout", comment = "Delay between repeated executing in milliseconds",
                 defaultValue = "(45*60+(Math.floor(Math.random()*111)+10))*1e3")
    private String sServerTimeout;

    @ConfigValue(value = "service.server.update.limit", comment = "Maximum number of updates per node",
                 defaultValue = "2500")
    private String sServerUpdateLimit;

    @ConfigValue(value = "service.server.core.update.limit", comment = "Maximum number of server core updates",
                 defaultValue = "9999")
    private String sServerCoreUpdateLimit;

    @ConfigValue(value = "service.server.purchase.packages", comment = "Purchase packages for netcoins",
                 defaultValue = "True")
    private String sServerBuyPackagesForNetcoins;

    // služba - store

    @ConfigValue(value = "service.store.enable", comment = "Enables shop control to purchase apps",
                 defaultValue = "True")
    private String sStoreEnable;

    @ConfigValue(value = "service.store.timeout", comment = "Delay between repeated executing in milliseconds",
                 defaultValue = "(60+(Math.floor(Math.random()*51)+10))*1e3")
    private String sStoreTimeout;

    @ConfigValue(value = "service.store.updated.apps",
                 comment = "List of applications to update separated by a comma\nSupported applications are: ${cz.ophite.mimic.vhackos.botnet.shared.dto.AppStoreType.UPDATABLE_APP_CODES}",
                 defaultValue = "[${cz.ophite.mimic.vhackos.botnet.shared.dto.AppStoreType.UPDATABLE_APP_CODES}]")
    private String sUpdatedAppsList;

    // služba - booster

    @ConfigValue(value = "service.booster.enable", comment = "Enables use of boosters for active tasks",
                 defaultValue = "True")
    private String sBoosterEnable;

    @ConfigValue(value = "service.booster.timeout", comment = "Delay between repeated executing in milliseconds",
                 defaultValue = "(5*60+(Math.floor(Math.random()*51)+10))*1e3")
    private String sBoosterTimeout;

    @ConfigValue(value = "service.booster.req.time",
                 comment = "Required task time for booster use. If at least one task time is greater than this value, then boost will be used\nTime is in seconds",
                 defaultValue = "900")
    private String sBoosterReqTime;

    // služba - mission

    @ConfigValue(value = "service.mission.enable",
                 comment = "Enables automatic completion of missions and picking up rewards", defaultValue = "True")
    private String sMissionEnable;

    @ConfigValue(value = "service.mission.timeout", comment = "Delay between repeated executing in milliseconds",
                 defaultValue = "(6*60*60+(Math.floor(Math.random()*3001)+600))*1e3")
    private String sMissionTimeout;

    // služba - network

    @ConfigValue(value = "service.network.enable",
                 comment = "Enable exploit, target bank hack, and cash withdrawal from the bank",
                 defaultValue = "False")
    private String sNetworkEnable;

    @ConfigValue(value = "service.network.timeout", comment = "Delay between repeated executing in milliseconds",
                 defaultValue = "(60*60+(Math.floor(Math.random()*2401)+1200))*1e3")
    private String sNetworkTimeout;

    // služba - netscan

    @ConfigValue(value = "service.netscan.enable",
                 comment = "Enable network scanning and data storage in the database\nFor this service, you need to have a configured database, otherwise it does not make sense to turn it on",
                 defaultValue = "False")
    private String sNetworkScanEnable;

    @ConfigValue(value = "service.netscan.timeout", comment = "Delay between repeated executing in milliseconds",
                 defaultValue = "(5+(Math.floor(Math.random()*11)+5))*1e3")
    private String sNetworkScanTimeout;

    // databáze

    @ConfigValue(value = "db.enable", comment = "Enable database storage", defaultValue = "False")
    private String dbEnable;

    @ConfigValue(value = "db.host", comment = "IP server running MySQL", defaultValue = "127.0.0.1:3306")
    private String dbHost;

    @ConfigValue(value = "db.user", comment = "Login a database user", defaultValue = "root")
    private String dbUser;

    @ConfigValue(value = "db.password", comment = "The database user password")
    private String dbPassword;

    @ConfigValue(value = "db.database", comment = "Database name for data storage", defaultValue = "vhackbotnet")
    private String dbDatabase;

    /**
     * Přemapuje konfigurační hodnoty z jiné konfigurace.
     */
    public void set(ApplicationConfig config) {
        var fields = getClass().getDeclaredFields();
        for (var field : fields) {
            if (field.isAnnotationPresent(ConfigValue.class)) {
                field.setAccessible(true);
                try {
                    field.set(this, getClass().getDeclaredField(field.getName()).get(config));
                } catch (Exception e) {
                    throw new IllegalStateException("An unexpected error has occurred", e);
                }
            }
        }
    }

    /**
     * Vyhodnotí, zda má uživatel v konfiguraci vyplněné uživatelské jméno a heslo.
     */
    public boolean hasValidCredentials() {
        if (StringUtils.isEmpty(getUserName()) || StringUtils.isEmpty(getPassword())) {
            return false;
        }
        return !DEFAULT_USERNAME.equalsIgnoreCase(getUserName()) && !DEFAULT_PASSWORD.equalsIgnoreCase(getPassword());
    }

    @Override
    public String getUserName() {
        return userName;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public String getDbHost() {
        return dbHost;
    }

    public String getDbUser() {
        return dbUser;
    }

    public String getDbPassword() {
        return dbPassword;
    }

    public String getDbDatabase() {
        return dbDatabase;
    }

    @Override
    public String getMessageLog() {
        return messageLog;
    }

    @Override
    public int getMaxRequestAttempts() {
        return Integer.valueOf(maxRequestAttempts);
    }

    @Override
    public long getSleepDelay() {
        return ((Double) SharedUtils.eval(sleepDelay)).longValue();
    }

    @Override
    public ProxyData getProxyData() {
        var enable = Boolean.valueOf(dbEnable);
        if (enable && !proxyHost.isEmpty() && !proxyPort.isEmpty()) {
            var proxy = new ProxyData();
            proxy.setIp(proxyHost);
            proxy.setPort(Integer.valueOf(proxyPort));
            return proxy;
        }
        return null;
    }

    public String getFixedAccessToken() {
        return StringUtils.isNotEmpty(fixedAccessToken) ? fixedAccessToken : null;
    }

    public Integer getFixedUserUid() {
        return StringUtils.isNotEmpty(fixedUserUid) ? Integer.valueOf(fixedUserUid) : null;
    }

    public boolean isDbEnable() {
        return Boolean.valueOf(dbEnable);
    }

    public long getUpdateTimeout() {
        return ((Double) SharedUtils.eval(sUpdateTimeout)).longValue();
    }

    public boolean isMinerEnable() {
        return Boolean.valueOf(sMinerEnable);
    }

    public long getMinerTimeout() {
        return ((Double) SharedUtils.eval(sMinerTimeout)).longValue();
    }

    public boolean isMalwareEnable() {
        return Boolean.valueOf(sMalwareEnable);
    }

    public long getMalwareTimeout() {
        return ((Double) SharedUtils.eval(sMalwareTimeout)).longValue();
    }

    public boolean isServerEnable() {
        return Boolean.valueOf(sServerEnable);
    }

    public long getServerTimeout() {
        return ((Double) SharedUtils.eval(sServerTimeout)).longValue();
    }

    public int getServerUpdateLimit() {
        return (Integer) SharedUtils.eval(sServerUpdateLimit);
    }

    public int getServerCoreUpdateLimit() {
        return (Integer) SharedUtils.eval(sServerCoreUpdateLimit);
    }

    public int getSafeNetcoins() {
        return (Integer) SharedUtils.eval(safeNetcoins);
    }

    public int getSafeBoosters() {
        return (Integer) SharedUtils.eval(safeBoosters);
    }

    public boolean isServerBuyPackagesForNetcoins() {
        return Boolean.valueOf(sServerBuyPackagesForNetcoins);
    }

    public boolean isStoreEnable() {
        return Boolean.valueOf(sStoreEnable);
    }

    public long getStoreTimeout() {
        return ((Double) SharedUtils.eval(sStoreTimeout)).longValue();
    }

    public List<AppStoreType> getUpdatedAppsList() {
        var list = sUpdatedAppsList.substring(1, sUpdatedAppsList.length() - 1);
        if (list.contains(",")) {
            var tokens = list.split(",");
            var apps = new HashSet<AppStoreType>(tokens.length);
            for (var t : tokens) {
                var type = AppStoreType.getByCode(t.toUpperCase().trim());
                if (type != null) {
                    apps.add(type);
                }
            }
            return new ArrayList<>(apps);
        }
        return Collections.emptyList();
    }

    public boolean isBoosterEnable() {
        return Boolean.valueOf(sBoosterEnable);
    }

    public long getBoosterTimeout() {
        return ((Double) SharedUtils.eval(sBoosterTimeout)).longValue();
    }

    public int getBoosterReqTime() {
        return (Integer) SharedUtils.eval(sBoosterReqTime);
    }

    public boolean isMissionEnable() {
        return Boolean.valueOf(sMissionEnable);
    }

    public long getMissionTimeout() {
        return ((Double) SharedUtils.eval(sMissionTimeout)).longValue();
    }

    public boolean isNetworkEnable() {
        return Boolean.valueOf(sNetworkEnable);
    }

    public long getNetworkTimeout() {
        return ((Double) SharedUtils.eval(sNetworkTimeout)).longValue();
    }

    public boolean isNetworkScanEnable() {
        return Boolean.valueOf(sNetworkScanEnable);
    }

    public long getNetworkScanTimeout() {
        return ((Double) SharedUtils.eval(sNetworkScanTimeout)).longValue();
    }
}
