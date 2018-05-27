package cz.ophite.mimic.vhackos.botnet.db.dao.base;

import cz.ophite.mimic.vhackos.botnet.db.HibernateManager;
import cz.ophite.mimic.vhackos.botnet.db.exception.DatabaseConnectionException;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.Entity;
import java.lang.reflect.ParameterizedType;

/**
 * Základní dao objekt.
 *
 * @author mimic
 */
public abstract class Dao<E> {

    private final Logger log;
    private final Class entityClass;

    protected Dao() {
        log = LoggerFactory.getLogger(getClass());
        entityClass = (Class) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    /**
     * Aktualizuje databázovou entitu (entita musí již existovat).
     */
    public final void update(E entity) {
        execute(s -> createOrUpdate(s, entity, false));
    }

    /**
     * Získá instanci loggeru.
     */
    protected final Logger getLog() {
        return log;
    }

    /**
     * Je připojení do databáze aktivní?
     */
    protected final boolean isConnected() {
        return HibernateManager.isConnected();
    }

    /**
     * Vykoná databázový příkaz.
     */
    protected final <T> T execute(ITransactionOnSession tos) {
        if (!HibernateManager.isConnected()) {
            throw new DatabaseConnectionException("There was an error connecting to the database");
        }
        var session = HibernateManager.getSessionFactory().openSession();
        var tx = session.beginTransaction();

        try (session) {
            var result = tos.execute(session);
            tx.commit();
            return (T) result;

        } catch (Exception e) {
            tx.rollback();
            tos.error(this, e);
        }
        return null;
    }

    protected final String query(String query) {
        String name = ((Entity) entityClass.getDeclaredAnnotation(Entity.class)).name();
        name = query.replaceAll("\\{entity\\}", name);
        return name;
    }

    /**
     * Vytvoří nebo aktualizuje databázovo uentitu.
     */
    protected final E createOrUpdate(Session session, E entity, boolean createNew) {
        if (createNew) {
            session.persist(entity);
        } else {
            session.update(entity);
        }
        return entity;
    }

    /**
     * Rozhraní pro tělo databázového příkazu v aktivní transakci.
     */
    protected interface ITransactionOnSession {

        /**
         * Vykoná databázový příkaz.
         */
        Object execute(Session session);

        /**
         * Při zpracování dotazu nastala chyba.
         */
        default void error(Dao dao, Exception e) {
            dao.log.error("There was an error processing the database query", e);
        }
    }
}
