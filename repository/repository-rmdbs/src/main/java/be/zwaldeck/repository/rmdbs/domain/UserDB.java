package be.zwaldeck.repository.rmdbs.domain;

import be.zwaldeck.zcms.repository.api.model.Role;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "user_tbl")
@Data
public class UserDB {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    private String id;

    @Enumerated
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles_tbl", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"))
    private List<Role> roles;

    @Column(name = "username", unique = true, nullable = false)
    private String userName;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "created_at", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createAt;

    @Column(name = "updated_at", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateAt;

    @PrePersist
    public void prePersists() {
        this.createAt = new Date();
        this.updateAt = new Date();
    }

    @PreUpdate
    public void preUpdate() {
        this.updateAt = new Date();
    }
}
