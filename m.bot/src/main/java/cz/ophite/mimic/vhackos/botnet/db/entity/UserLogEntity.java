package cz.ophite.mimic.vhackos.botnet.db.entity;

import cz.ophite.mimic.vhackos.botnet.db.entity.base.PersistentEntity;

import javax.persistence.*;

/**
 * Databázová entita mapující uživatele na jeho log.
 *
 * @author mimic
 */
@Table(name = UserLogEntity.TABLE_SQL)
@Entity(name = UserLogEntity.TABLE)
public final class UserLogEntity extends PersistentEntity {

    static final String TABLE = "UserLog";
    static final String TABLE_SQL = "user_log";

    @ManyToOne
    @JoinColumn(name = USER_SQL)
    private UserEntity user;
    public static final String USER = "user";
    static final String USER_SQL = "user_id";

    @Lob
    @Column(name = LOG_SQL, nullable = false)
    private String log;
    public static final String LOG = "log";
    static final String LOG_SQL = "log";

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public String getLog() {
        return log;
    }

    public void setLog(String log) {
        this.log = log;
    }
}
