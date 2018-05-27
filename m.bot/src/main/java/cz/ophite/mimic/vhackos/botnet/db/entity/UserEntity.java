package cz.ophite.mimic.vhackos.botnet.db.entity;

import cz.ophite.mimic.vhackos.botnet.db.entity.base.PersistentEntity;
import cz.ophite.mimic.vhackos.botnet.shared.utils.ascii.AsciiRow;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

/**
 * Databázová entita uživatele.
 *
 * @author mimic
 */
@Table(name = UserEntity.TABLE_SQL, uniqueConstraints = @UniqueConstraint(columnNames = {
        UserEntity.USER_ID_SQL, UserEntity.USER_NAME_SQL }))
@Entity(name = UserEntity.TABLE)
public final class UserEntity extends PersistentEntity {

    static final String TABLE = "User";
    public static final String TABLE_SQL = "user";

    @AsciiRow("User ID")
    @Column(name = USER_ID_SQL, length = 10)
    private Integer userId;
    public static final String USER_ID = "userId";
    public static final String USER_ID_SQL = "user_id";

    @AsciiRow("User")
    @Column(name = USER_NAME_SQL, length = 50)
    private String userName;
    public static final String USER_NAME = "userName";
    static final String USER_NAME_SQL = "user_name";

    @CreationTimestamp
    @Temporal(value = TemporalType.TIMESTAMP)
    @Column(name = CREATED_SQL, nullable = false)
    private Date created;
    public static final String CREATED = "created";
    private static final String CREATED_SQL = "created";

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = UserIpEntity.TABLE_SQL, joinColumns = { @JoinColumn(name = UserIpEntity.USER_SQL) })
    @Column(name = UserIpEntity.IP_SQL)
    private Set<String> ips;
    public static final String IPS = "ips";

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = UserLogEntity.TABLE_SQL, joinColumns = { @JoinColumn(name = UserLogEntity.USER_SQL) })
    @Column(name = UserLogEntity.LOG_SQL)
    private Set<String> logs;
    public static final String LOGS = "logs";

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = TransactionEntity.FROM_USER_SQL)
    private Set<TransactionEntity> transactionsFrom;
    public static final String TRANSACTIONS_FROM = "transactionsFrom";

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = TransactionEntity.TO_USER_SQL)
    private Set<TransactionEntity> transactionsTo;
    public static final String TRANSACTIONS_TO = "transactionsTo";

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Set<String> getIps() {
        return ips;
    }

    public void setIps(Set<String> ips) {
        this.ips = ips;
    }

    public Set<TransactionEntity> getTransactionsFrom() {
        return transactionsFrom;
    }

    public void setTransactionsFrom(Set<TransactionEntity> transactionsFrom) {
        this.transactionsFrom = transactionsFrom;
    }

    public Set<TransactionEntity> getTransactionsTo() {
        return transactionsTo;
    }

    public void setTransactionsTo(Set<TransactionEntity> transactionsTo) {
        this.transactionsTo = transactionsTo;
    }

    public Set<String> getLogs() {
        return logs;
    }

    public void setLogs(Set<String> logs) {
        this.logs = logs;
    }
}
