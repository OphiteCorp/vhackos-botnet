package cz.ophite.mimic.vhackos.botnet.db.dao;

import cz.ophite.mimic.vhackos.botnet.db.dao.base.Dao;
import cz.ophite.mimic.vhackos.botnet.db.entity.TransactionEntity;
import cz.ophite.mimic.vhackos.botnet.db.entity.UserEntity;
import cz.ophite.mimic.vhackos.botnet.shared.injection.Inject;

import java.util.Date;

/**
 * Práce s DB entitou bankovních transakcí.
 *
 * @author mimic
 */
@Inject
public final class TransactionDao extends Dao<TransactionEntity> {

    /**
     * Vytvoří záznam v databázi.
     */
    public TransactionEntity create(Date time, UserEntity fromUser, String fromUserIp, UserEntity toUser,
            String toUserIp, long amount) {

        var entity = new TransactionEntity();
        entity.setTime(time);
        entity.setFromUser(fromUser);
        entity.setFromUserIp(fromUserIp);
        entity.setToUser(toUser);
        entity.setToUserIp(toUserIp);
        entity.setAmount(amount);
        entity.setCreated(new Date());

        return execute(s -> createOrUpdate(s, entity, true));
    }

    /**
     * Vyhodnotí, zda existuje transakce, která má konkrétní ID uživatele "z" a "do".
     */
    public boolean isExistsByUserIds(int fromUserId, int toUserId) {
        return execute(s -> {
            var hql = "select count(t) from {entity} t ";
            hql += "join t." + TransactionEntity.FROM_USER + " fu ";
            hql += "join t." + TransactionEntity.TO_USER + " tu ";
            hql += "where fu." + UserEntity.USER_ID + " = :FROM_ID and ";
            hql += "tu." + UserEntity.USER_ID + " = :TO_ID";

            var q = s.createQuery(query(hql));
            q.setParameter("FROM_ID", fromUserId);
            q.setParameter("TO_ID", toUserId);
            return ((long) q.uniqueResult()) > 0;
        });
    }
}
