package cz.ophite.mimic.vhackos.botnet.db;

import cz.ophite.mimic.vhackos.botnet.config.ApplicationConfig;
import cz.ophite.mimic.vhackos.botnet.db.entity.UserEntity;
import cz.ophite.mimic.vhackos.botnet.db.exception.DatabaseConnectionException;
import cz.ophite.mimic.vhackos.botnet.shared.utils.SentryGuard;
import org.apache.logging.log4j.util.Strings;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.service.ServiceRegistry;
import org.reflections.Reflections;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.Entity;
import java.sql.DriverManager;
import java.util.Arrays;
import java.util.Properties;

/**
 * Pomocné metody pro práci s hibernate.
 *
 * @author mimic
 */
public final class HibernateManager {

    private static final Logger LOG = LoggerFactory.getLogger(HibernateManager.class);

    private static final String MYSQL_DRIVER = "com.mysql.cj.jdbc.Driver";

    private static SessionFactory sessionFactory;
    private static ServiceRegistry serviceRegistry;
    private static boolean created;

    /**
     * Vytvoří potřebné hibernate instance.
     */
    public static synchronized void initialize(ApplicationConfig config) throws DatabaseConnectionException {
        if (created) {
            return;
        }
        checkDatabaseConnection(config);
        created = true;

        // definice parametru pro připojení k databázi
        var paramsList = Arrays.asList( //
                "createDatabaseIfNotExist=true", //
                "useLegacyDatetimeCode=false", //
                "serverTimezone=Europe/Rome", //
                "useUnicode=true", //
                "characterEncoding=UTF-16", //
                "useSSL=false");
        var params = "?" + Strings.join(paramsList, '&');
        var connUrl = String.format("jdbc:mysql://%s/%s%s", config.getDbHost(), config.getDbDatabase(), params);

        // konfigurace hibernate
        Properties prop = new Properties();
        prop.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL5InnoDBDialect");
        prop.setProperty("hibernate.connection.driver_class", MYSQL_DRIVER);
        prop.setProperty("hibernate.connection.username", config.getDbUser());
        prop.setProperty("hibernate.connection.password", config.getDbPassword());
        prop.setProperty("hibernate.connection.url", connUrl);
        prop.setProperty("hibernate.hbm2ddl.auto", "update");
        prop.setProperty("hibernate.bytecode.use_reflection_optimizer", "true");
        prop.setProperty("hibernate.current_session_context_class", "org.hibernate.context.internal.ThreadLocalSessionContext");
        prop.setProperty("hibernate.c3p0.min_size", "0");
        prop.setProperty("hibernate.c3p0.max_size", "15");
        prop.setProperty("hibernate.c3p0.acquire_increment", "1");
        prop.setProperty("hibernate.c3p0.max_statements", "100");
        prop.setProperty("hibernate.c3p0.timeout", "2500");
        prop.setProperty("hibernate.c3p0.idle_test_period", "300");
        prop.setProperty("connection.autocommit", "false");
        prop.setProperty("show_sql", "false");

        try {
            serviceRegistry = new StandardServiceRegistryBuilder().applySettings(prop).build();
            LOG.debug("The hibernate service has been registered");

            var sources = new MetadataSources(serviceRegistry);
            var ref = new Reflections(UserEntity.class.getPackage().getName(), new TypeAnnotationsScanner());
            var classes = ref.getTypesAnnotatedWith(Entity.class, true);

            for (var clazz : classes) {
                sources.addAnnotatedClass(clazz);
            }
            var builder = sources.getMetadataBuilder();
            var metadata = builder.build();

            sessionFactory = metadata.getSessionFactoryBuilder().build();
            LOG.info("Session factory for hibernate was created");

        } catch (Exception e) {
            SentryGuard.log(e);
            LOG.error("An error occurred while creating a session factory for hibernate", e);
            shutdown();
        }
    }

    /**
     * Zísiá instanci session factory.
     */
    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    /**
     * Je připojení do databáze aktivní?
     */
    public static boolean isConnected() {
        return (sessionFactory != null && sessionFactory.isOpen());
    }

    /**
     * Zruší registraci pro hibernate.
     */
    public static void shutdown() {
        LOG.debug("Disconnecting MySQL database");
        if (sessionFactory != null) {
            sessionFactory.close();
        }
        if (serviceRegistry != null) {
            StandardServiceRegistryBuilder.destroy(serviceRegistry);
        }
    }

    private static void checkDatabaseConnection(ApplicationConfig config) {
        try {
            var clazz = Class.forName(MYSQL_DRIVER);
            clazz.getConstructor().newInstance();

            var simpleConnUrl = String
                    .format("jdbc:mysql://%s?serverTimezone=Europe/Rome&useSSL=false&user=%s&password=%s", config
                            .getDbHost(), config.getDbUser(), config.getDbPassword());
            var connection = DriverManager.getConnection(simpleConnUrl);
            connection.close();

        } catch (Exception e) {
            throw new DatabaseConnectionException("Could not create database connection", e);
        }
    }
}
