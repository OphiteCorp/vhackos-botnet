package cz.ophite.mimic.vhackos.botnet.db.entity.base;

import javax.persistence.*;

/**
 * Základní databázová entita.
 *
 * @author mimic
 */
@MappedSuperclass
public abstract class PersistentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    public static final String ID = "id";

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
