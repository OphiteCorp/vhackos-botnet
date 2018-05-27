package cz.ophite.mimic.vhackos.botnet.db.entity;

import cz.ophite.mimic.vhackos.botnet.db.entity.base.PersistentEntity;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

/**
 * @author mimic
 */
@Table(name = TransactionEntity.TABLE_SQL)
@Entity(name = TransactionEntity.TABLE)
public final class TransactionEntity extends PersistentEntity {

    static final String TABLE = "Transaction";
    static final String TABLE_SQL = "transaction";

    @Temporal(value = TemporalType.TIMESTAMP)
    @Column(name = TIME_SQL, nullable = false)
    private Date time;
    public static final String TIME = "time";
    private static final String TIME_SQL = "time";

    @ManyToOne
    @JoinColumn(name = FROM_USER_SQL)
    private UserEntity fromUser;
    public static final String FROM_USER = "fromUser";
    static final String FROM_USER_SQL = "from_user_id";

    @Column(name = FROM_USER_IP_SQL, length = 15, nullable = false)
    private String fromUserIp;
    public static final String FROM_USER_IP = "fromUserIp";
    private static final String FROM_USER_IP_SQL = "from_user_ip";

    @ManyToOne
    @JoinColumn(name = TO_USER_SQL)
    private UserEntity toUser;
    public static final String TO_USER = "toUser";
    static final String TO_USER_SQL = "to_user_id";

    @Column(name = TO_USER_IP_SQL, length = 15, nullable = false)
    private String toUserIp;
    public static final String TO_USER_IP = "toUserIp";
    private static final String TO_USER_IP_SQL = "to_user_ip";

    @Column(name = AMOUNT_SQL, nullable = false)
    private long amount;
    public static final String AMOUNT = "amount";
    private static final String AMOUNT_SQL = "amount";

    @CreationTimestamp
    @Temporal(value = TemporalType.TIMESTAMP)
    @Column(name = CREATED_SQL, nullable = false)
    private Date created;
    public static final String CREATED = "created";
    private static final String CREATED_SQL = "created";

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public UserEntity getFromUser() {
        return fromUser;
    }

    public void setFromUser(UserEntity fromUser) {
        this.fromUser = fromUser;
    }

    public UserEntity getToUser() {
        return toUser;
    }

    public void setToUser(UserEntity toUser) {
        this.toUser = toUser;
    }

    public String getFromUserIp() {
        return fromUserIp;
    }

    public void setFromUserIp(String fromUserIp) {
        this.fromUserIp = fromUserIp;
    }

    public String getToUserIp() {
        return toUserIp;
    }

    public void setToUserIp(String toUserIp) {
        this.toUserIp = toUserIp;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }
}
