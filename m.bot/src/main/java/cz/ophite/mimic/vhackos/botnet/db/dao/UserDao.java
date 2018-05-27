package cz.ophite.mimic.vhackos.botnet.db.dao;

import cz.ophite.mimic.vhackos.botnet.db.dao.base.Dao;
import cz.ophite.mimic.vhackos.botnet.db.entity.UserEntity;
import cz.ophite.mimic.vhackos.botnet.db.entity.UserIpEntity;
import cz.ophite.mimic.vhackos.botnet.shared.injection.Inject;
import cz.ophite.mimic.vhackos.botnet.utils.Utils;

import java.util.Date;
import java.util.HashSet;
import java.util.List;

/**
 * Práce s DB entitou uživatele.
 *
 * @author mimic
 */
@Inject
public final class UserDao extends Dao<UserEntity> {

    /**
     * Vytvoří nový záznam uživatele.
     */
    public UserEntity create(int userId, String ip) {
        var user = getByUserId(userId);

        if (user == null) {
            user = new UserEntity();
            user.setUserId(userId);
            user.setUserName(null);
            user.setIps(new HashSet<>());
            user.setCreated(new Date());

            if (Utils.isValidIp(ip)) {
                user.getIps().add(ip);
            }
            var finalUser = user;
            user = execute(s -> createOrUpdate(s, finalUser, true));
        }
        return user;
    }

    /**
     * Vytvoří nový záznam uživatele.
     */
    public UserEntity createOrUpdate(Integer userId, String userName, String ip) {
        var user = getByUserName(userName);

        if (user == null) {
            user = new UserEntity();
            user.setIps(new HashSet<>());
        }
        if (userName != null) {
            user.setUserName(userName);
        }
        if (Utils.isValidIp(ip)) {
            user.setUserId(userId);
        }
        user.setCreated(new Date());
        user.getIps().add(ip);

        var finalUser = user;
        return execute(s -> createOrUpdate(s, finalUser, finalUser.getId() == null));
    }

    /**
     * Získá všechny uživatele.
     */
    public List<UserEntity> getAll() {
        return execute(s -> {
            var q = s.createQuery(query("select u from {entity} u"), UserEntity.class);
            return q.list();
        });
    }

    /**
     * Získá uživatele podle jeho IP.
     */
    public List<UserEntity> getByIp(String ip) {
        return execute(s -> {
            var hql = query("select u from {entity} u ");
            hql += "join fetch u." + UserEntity.IPS + " ips where ips in (:IP) ";
            hql += "order by u." + UserEntity.ID + " desc";

            var q = s.createQuery(hql, UserEntity.class);
            q.setParameter("IP", ip);
            return q.list();
        });
    }

    /**
     * Získá uživatele podle jeho jména.
     */
    public UserEntity getByUserName(String userName) {
        var hql = query("select u from {entity} u where u." + UserEntity.USER_NAME + " = :USER_NAME");

        return execute(s -> {
            var q = s.createQuery(hql, UserEntity.class);
            q.setParameter("USER_NAME", userName);
            return q.uniqueResult();
        });
    }

    /**
     * Získá uživatele podle jeho uživatelského ID.
     */
    private UserEntity getByUserId(int userId) {
        var hql = query("select u from {entity} u where u." + UserEntity.USER_ID + " = :USER_ID");

        return execute(s -> {
            var q = s.createQuery(hql, UserEntity.class);
            q.setParameter("USER_ID", userId);
            return q.uniqueResult();
        });
    }

    /**
     * Získá všechny IP uživatele podke ID seřazené od nejnovější.
     */
    public List<String> getIps(int userId) {
        return execute(s -> {
            var sql = "select ips." + UserIpEntity.IP_SQL + " from " + UserIpEntity.TABLE_SQL + " ips ";
            sql += "join " + UserEntity.TABLE_SQL + " u on u." + UserEntity.ID + " = ips." + UserIpEntity.USER_SQL + " ";
            sql += "where u." + UserEntity.USER_ID_SQL + " = :ID ";
            sql += "order by ips." + UserIpEntity.ID + " desc";

            var q = s.createNativeQuery(sql);
            q.setParameter("ID", userId);
            return q.list();
        });
    }
}
