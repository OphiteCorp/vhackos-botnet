package cz.ophite.mimic.vhackos.botnet.db.entity;

import cz.ophite.mimic.vhackos.botnet.db.entity.base.PersistentEntity;

import javax.persistence.*;

/**
 * Databázová entita mapující IP na uživatele.
 *
 * @author mimic
 */
@Table(name = UserIpEntity.TABLE_SQL, uniqueConstraints = @UniqueConstraint(columnNames = {
        UserIpEntity.USER_SQL, UserIpEntity.IP_SQL }))
@Entity(name = UserIpEntity.TABLE)
public final class UserIpEntity extends PersistentEntity {

    static final String TABLE = "UserIp";
    public static final String TABLE_SQL = "user_ip";

    @ManyToOne
    @JoinColumn(name = USER_SQL)
    private UserEntity user;
    public static final String USER = "user";
    public static final String USER_SQL = "user_id";

    @Column(name = IP_SQL, length = 15, nullable = false)
    private String ip;
    public static final String IP = "ip";
    public static final String IP_SQL = "ip";

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
}
