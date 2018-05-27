package cz.ophite.mimic.vhackos.botnet.db.entity;

import cz.ophite.mimic.vhackos.botnet.db.entity.base.PersistentEntity;
import cz.ophite.mimic.vhackos.botnet.shared.utils.ascii.AsciiRow;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

/**
 * Databázová entita s informace o naskenované IP.
 *
 * @author mimic
 */
@Table(name = ScannedIpEntity.TABLE_SQL)
@Entity(name = ScannedIpEntity.TABLE)
public final class ScannedIpEntity extends PersistentEntity {

    static final String TABLE = "ScannedIp";
    static final String TABLE_SQL = "scanned_ip";

    @AsciiRow("IP")
    @Column(name = IP_SQL, length = 15, unique = true)
    private String ip;
    public static final String IP = "ip";
    private static final String IP_SQL = "ip";

    @AsciiRow("Level")
    @Column(name = LEVEL_SQL, nullable = false)
    private int level;
    public static final String LEVEL = "level";
    private static final String LEVEL_SQL = "level";

    @AsciiRow("Firewall")
    @Column(name = FIREWALL_SQL, nullable = false)
    private int firewall;
    public static final String FIREWALL = "firewall";
    private static final String FIREWALL_SQL = "firewall";

    @AsciiRow("User")
    @Column(name = USER_NAME_SQL, length = 50)
    private String userName;
    public static final String USER_NAME = "userName";
    private static final String USER_NAME_SQL = "user_name";

    @CreationTimestamp
    @Temporal(value = TemporalType.TIMESTAMP)
    @Column(name = CREATED_SQL, nullable = false)
    private Date created;
    public static final String CREATED = "created";
    private static final String CREATED_SQL = "created";

    @Temporal(value = TemporalType.TIMESTAMP)
    @Column(name = UPDATED_SQL)
    private Date updated;
    public static final String UPDATED = "updated";
    private static final String UPDATED_SQL = "updated";

    @Column(name = VALID_SQL, nullable = false)
    private boolean valid;
    public static final String VALID = "valid";
    private static final String VALID_SQL = "valid";

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getFirewall() {
        return firewall;
    }

    public void setFirewall(int firewall) {
        this.firewall = firewall;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }
}
