package cz.ophite.mimic.vhackos.botnet.db.dao;

import cz.ophite.mimic.vhackos.botnet.db.dao.base.Dao;
import cz.ophite.mimic.vhackos.botnet.db.entity.ScannedIpEntity;
import cz.ophite.mimic.vhackos.botnet.shared.injection.Inject;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;
import java.util.List;

/**
 * Práce s DB entitou skenovaných IP.
 *
 * @author mimic
 */
@Inject
public final class ScannedIpDao extends Dao<ScannedIpEntity> {

    /**
     * Vytvoří nebo aktualizuje záznam v databázi.
     */
    public ScannedIpEntity createOrUpdate(String ip, int level, int firewall, String userName) {
        var entity = getByIp(ip);

        if (entity == null) {
            entity = new ScannedIpEntity();
            entity.setIp(ip);
            entity.setCreated(new Date());
        } else {
            entity.setUpdated(new Date());
        }
        entity.setValid(true);
        entity.setLevel(level);
        entity.setFirewall(firewall);

        if (userName != null) {
            entity.setUserName(userName);
        }
        var finalEntity = entity;
        return execute(s -> createOrUpdate(s, finalEntity, finalEntity.getUpdated() == null));
    }

    /**
     * Získá všechny naskenované IP.
     */
    public List<ScannedIpEntity> getScannedIps(String orderColumn) {
        return execute(s -> {
            var sb = new StringBuilder();
            sb.append("select sip from {entity} sip ");

            if (StringUtils.isEmpty(orderColumn)) {
                sb.append("order by sip." + ScannedIpEntity.LEVEL + " desc, sip." + ScannedIpEntity.FIREWALL + " desc");
            } else {
                sb.append("order by sip.").append(orderColumn).append(" desc");
            }
            var q = s.createQuery(query(sb.toString()), ScannedIpEntity.class);
            return q.list();
        });
    }

    /**
     * Získá naskenovanou IP podle IP.
     */
    public ScannedIpEntity getByIp(String ip) {
        var hql = query("select sip from {entity} sip where sip." + ScannedIpEntity.IP + " = :IP");

        return execute(s -> {
            var q = s.createQuery(hql, ScannedIpEntity.class);
            q.setParameter("IP", ip);
            return q.uniqueResult();
        });
    }

    /**
     * Získá všechny naskenované IP bez uživatele.
     */
    public List<ScannedIpEntity> getIpsWithoutUser(int limit) {
        return execute(s -> {
            var sb = new StringBuilder();
            sb.append("select sip from {entity} sip ");
            sb.append("where sip." + ScannedIpEntity.USER_NAME + " is null");

            var q = s.createQuery(query(sb.toString()));
            q.setMaxResults(limit);
            return q.list();
        });
    }
}
