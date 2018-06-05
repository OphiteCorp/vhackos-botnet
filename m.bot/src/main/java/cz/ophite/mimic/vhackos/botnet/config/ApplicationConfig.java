package cz.ophite.mimic.vhackos.botnet.config;

import cz.ophite.mimic.vhackos.botnet.api.IBotnetConfig;
import cz.ophite.mimic.vhackos.botnet.api.dto.ProxyData;
import cz.ophite.mimic.vhackos.botnet.shared.dto.AppStoreType;
import cz.ophite.mimic.vhackos.botnet.shared.injection.Inject;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Konfigurace aplikace.
 *
 * @author mimic
 */
@Inject
public final class ApplicationConfig implements IBotnetConfig {

    private static final int AGGRESSIVE_TIME = 250;
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

    @ConfigValue(value = "game.message.left.in.log", defaultValue = "I did not take the money!<br>by Ophite.botnet",
                 comment = "The message will be left in the remote system log. Use <br> for a new line")
    private String messageLog;

    @ConfigValue(value = "game.safe.netcoins", defaultValue = "5000",
                 comment = "The number of netcoins that not be touched")
    private String safeNetcoins;

    @ConfigValue(value = "game.safe.boosters", defaultValue = "20",
                 comment = "The number of boosters that not be touched")
    private String safeBoosters;

    // maximální počet pokusů na odeslání požadavku na vHack server

    @ConfigValue(value = "sys.max.request.attempts",
                 comment = "The maximum number of attempts to send the request to the server", defaultValue = "3")
    private String maxRequestAttempts;

    @ConfigValue(value = "sys.sleep.delay",
                 comment = "Delay before each server request in milliseconds.\nDefault: between 1-3s",
                 defaultValue = "f_rand(1000,3000)")
    private String sleepDelay;

    @ConfigValue(value = "sys.proxy.enable", comment = "Enables the use of a proxy server", defaultValue = "False")
    private String proxyEnable;

    @ConfigValue(value = "sys.proxy.host",
                 comment = "Proxy server IP address. Currently supports only HTTP connections", canBeEmpty = true)
    private String proxyHost;

    @ConfigValue(value = "sys.proxy.port", comment = "Proxy server port", canBeEmpty = true)
    private String proxyPort;

    @ConfigValue(value = "sys.aggressive.mode",
                 comment = "Enables aggressive mode. This mode greatly increases bot activity, but behavior no longer responds to player behavior",
                 defaultValue = "False")
    private String aggressiveMode;

    // GUI

    @ConfigValue(value = "gui.fullscreen.mode", comment = "Expands the application across the desktop",
                 defaultValue = "False")
    private String fullScreenMode;

    // služba - Botnet update

    @ConfigValue(value = "service-botnet-update.enable", comment = "Enable checking the latest version of Botnet",
                 defaultValue = "True")
    private String sBotnetUpdateEnable;

    // služba - update

    @ConfigValue(value = "service-update.timeout",
                 comment = "Delay between repeated executing in milliseconds.\nDefault: between 1.5h-3h",
                 defaultValue = "f_rand(5400000,10800000)")
    private String sUpdateTimeout;

    // služba - miner

    @ConfigValue(value = "service-miner.enable", comment = "Enable netcoins mining service", defaultValue = "False")
    private String sMinerEnable;

    @ConfigValue(value = "service-miner.timeout",
                 comment = "Delay between repeated executing in milliseconds.\nDefault: between 1h 5s and 1h 1min",
                 defaultValue = "f_rand(3605000,3660000)")
    private String sMinerTimeout;

    // služba - malware

    @ConfigValue(value = "service-malware.enable", comment = "Enable malware production service",
                 defaultValue = "False")
    private String sMalwareEnable;

    @ConfigValue(value = "service-malware.timeout",
                 comment = "Delay between repeated executing in milliseconds.\nDefault: between 10min 5s and 10min 30s",
                 defaultValue = "f_rand(605000,630000)")
    private String sMalwareTimeout;

    // služba - server

    @ConfigValue(value = "service-server.enable", comment = "Enable server control", defaultValue = "False")
    private String sServerEnable;

    @ConfigValue(value = "service-server.timeout",
                 comment = "Delay between repeated executing in milliseconds.\nDefault: between 49min and 51min",
                 defaultValue = "f_rand(2940000,3060000)")
    private String sServerTimeout;

    @ConfigValue(value = "service-server.update.limit", comment = "Maximum number of updates per node",
                 defaultValue = "2500")
    private String sServerUpdateLimit;

    @ConfigValue(value = "service-server.core.update.limit", comment = "Maximum number of server core updates",
                 defaultValue = "2500")
    private String sServerCoreUpdateLimit;

    @ConfigValue(value = "service-server.purchase.packages", comment = "Purchase packages for netcoins",
                 defaultValue = "True")
    private String sServerBuyPackagesForNetcoins;

    // služba - store

    @ConfigValue(value = "service-store.enable", comment = "Enables shop control to purchase apps",
                 defaultValue = "False")
    private String sStoreEnable;

    @ConfigValue(value = "service-store.timeout",
                 comment = "Delay between repeated executing in milliseconds.\nDefault: between 2min and 6min",
                 defaultValue = "f_rand(120000,360000)")
    private String sStoreTimeout;

    @ConfigValue(value = "service-store.updated.apps",
                 comment = "List of applications to update separated by a comma\nSupported applications are: ${cz.ophite.mimic.vhackos.botnet.shared.dto.AppStoreType.UPDATABLE_APP_CODES}",
                 defaultValue = "[${cz.ophite.mimic.vhackos.botnet.shared.dto.AppStoreType.UPDATABLE_APP_CODES}]")
    private String sUpdatedAppsList;

    // služba - booster

    @ConfigValue(value = "service-booster.enable", comment = "Enables use of boosters for active tasks",
                 defaultValue = "False")
    private String sBoosterEnable;

    @ConfigValue(value = "service-booster.timeout",
                 comment = "Delay between repeated executing in milliseconds.\nDefault: between 3min and 6min",
                 defaultValue = "f_rand(180000,360000)")
    private String sBoosterTimeout;

    @ConfigValue(value = "service-booster.req.time",
                 comment = "Required task time for booster use. If at least one task time is greater than this value, then boost will be used\nTime is in seconds. Default: 15min",
                 defaultValue = "900")
    private String sBoosterReqTime;

    // služba - mission

    @ConfigValue(value = "service-mission.enable",
                 comment = "Enables automatic completion of missions and picking up rewards", defaultValue = "False")
    private String sMissionEnable;

    @ConfigValue(value = "service-mission.timeout",
                 comment = "Delay between repeated executing in milliseconds.\nDefault: between 6h and 10h",
                 defaultValue = "f_rand(21600000,36000000)")
    private String sMissionTimeout;

    // služba - network

    @ConfigValue(value = "service-network.enable",
                 comment = "Enable exploit, target bank hack, and cash withdrawal from the bank",
                 defaultValue = "False")
    private String sNetworkEnable;

    @ConfigValue(value = "service-network.timeout",
                 comment = "Delay between repeated executing in milliseconds.\nDefault: between 50min 5s and 51min",
                 defaultValue = "f_rand(3005000,3060000)")
    private String sNetworkTimeout;

    // služba - netscan

    @ConfigValue(value = "service-netscan.enable",
                 comment = "Enable network scanning and data storage in the database\nFor this service, you need to have a configured database, otherwise it does not make sense to turn it on",
                 defaultValue = "False")
    private String sNetworkScanEnable;

    @ConfigValue(value = "service-netscan.timeout",
                 comment = "Delay between repeated executing in milliseconds.\nDefault: between 3-10s",
                 defaultValue = "f_rand(3000,10000)")
    private String sNetworkScanTimeout;

    @ConfigValue(value = "service-netscan.count.before.pause",
                 comment = "Number of network scanning before pause.\nDefault: between 2-3",
                 defaultValue = "f_rand(2,3)")
    private String sNetworkScanCountBeforePause;

    @ConfigValue(value = "service-netscan.pause",
                 comment = "Break interval between the number of scans in milliseconds.\nDefault: between 20-40s",
                 defaultValue = "f_rand(20000,40000)")
    private String sNetworkScanPause;

    @ConfigValue(value = "service-netscan.allow.brute",
                 comment = "Enables brute force IP. This method does not allow access to a bank, but only helps to obtain the user's name",
                 defaultValue = "True")
    private String sNetworkScanAllowBrute;

    @ConfigValue(value = "service-netscan.brute.prefer.database",
                 comment = "Enable if users are to prefer the database without a name before the new scan user",
                 defaultValue = "True")
    private String sNetworkScanPreferDatabase;

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
        if (isAggressiveMode()) {
            return AGGRESSIVE_TIME;
        }
        return ConfigHelper.getNumbericValue(sleepDelay, Long.class);
    }

    @Override
    public ProxyData getProxyData() {
        var enable = ConfigHelper.getBoolean(proxyEnable);

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
        return ConfigHelper.getBoolean(dbEnable);
    }

    public long getUpdateTimeout() {
        return ConfigHelper.getNumbericValue(sUpdateTimeout, Long.class);
    }

    public boolean isMinerEnable() {
        return ConfigHelper.getBoolean(sMinerEnable);
    }

    public long getMinerTimeout() {
        return ConfigHelper.getNumbericValue(sMinerTimeout, Long.class);
    }

    public boolean isMalwareEnable() {
        return ConfigHelper.getBoolean(sMalwareEnable);
    }

    public long getMalwareTimeout() {
        return ConfigHelper.getNumbericValue(sMalwareTimeout, Long.class);
    }

    public boolean isServerEnable() {
        return ConfigHelper.getBoolean(sServerEnable);
    }

    public long getServerTimeout() {
        return ConfigHelper.getNumbericValue(sServerTimeout, Long.class);
    }

    public int getServerUpdateLimit() {
        return ConfigHelper.getNumbericValue(sServerUpdateLimit, Integer.class);
    }

    public int getServerCoreUpdateLimit() {
        return ConfigHelper.getNumbericValue(sServerCoreUpdateLimit, Integer.class);
    }

    public int getSafeNetcoins() {
        return ConfigHelper.getNumbericValue(safeNetcoins, Integer.class);
    }

    public int getSafeBoosters() {
        return ConfigHelper.getNumbericValue(safeBoosters, Integer.class);
    }

    public boolean isServerBuyPackagesForNetcoins() {
        return ConfigHelper.getBoolean(sServerBuyPackagesForNetcoins);
    }

    public boolean isStoreEnable() {
        return ConfigHelper.getBoolean(sStoreEnable);
    }

    public long getStoreTimeout() {
        return ConfigHelper.getNumbericValue(sStoreTimeout, Long.class);
    }

    public List<AppStoreType> getUpdatedAppsList() {
        var list = sUpdatedAppsList.substring(1, sUpdatedAppsList.length() - 1).trim();
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

    public boolean isBoosterEnable() {
        return ConfigHelper.getBoolean(sBoosterEnable);
    }

    public long getBoosterTimeout() {
        return ConfigHelper.getNumbericValue(sBoosterTimeout, Long.class);
    }

    public int getBoosterReqTime() {
        return ConfigHelper.getNumbericValue(sBoosterReqTime, Integer.class);
    }

    public boolean isMissionEnable() {
        return ConfigHelper.getBoolean(sMissionEnable);
    }

    public long getMissionTimeout() {
        return ConfigHelper.getNumbericValue(sMissionTimeout, Long.class);
    }

    public boolean isNetworkEnable() {
        return ConfigHelper.getBoolean(sNetworkEnable);
    }

    public long getNetworkTimeout() {
        return ConfigHelper.getNumbericValue(sNetworkTimeout, Long.class);
    }

    public boolean isNetworkScanEnable() {
        return ConfigHelper.getBoolean(sNetworkScanEnable);
    }

    public long getNetworkScanTimeout() {
        return ConfigHelper.getNumbericValue(sNetworkScanTimeout, Long.class);
    }

    public boolean isFullScreenMode() {
        return ConfigHelper.getBoolean(fullScreenMode);
    }

    public int getNetworkScanCountBeforePause() {
        return ConfigHelper.getNumbericValue(sNetworkScanCountBeforePause, Integer.class);
    }

    public long getNetworkScanPause() {
        if (isAggressiveMode()) {
            return AGGRESSIVE_TIME;
        }
        return ConfigHelper.getNumbericValue(sNetworkScanPause, Long.class);
    }

    public boolean isNetworkScanAllowBrute() {
        return ConfigHelper.getBoolean(sNetworkScanAllowBrute);
    }

    @Override
    public boolean isAggressiveMode() {
        return ConfigHelper.getBoolean(aggressiveMode);
    }

    public boolean isBotnetUpdateEnable() {
        return ConfigHelper.getBoolean(sBotnetUpdateEnable);
    }

    public boolean isNetworkScanPreferDatabase() {
        return ConfigHelper.getBoolean(sNetworkScanPreferDatabase);
    }
}
