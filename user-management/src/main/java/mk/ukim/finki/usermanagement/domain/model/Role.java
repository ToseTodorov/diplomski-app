package mk.ukim.finki.usermanagement.domain.model;

import lombok.Getter;
import lombok.Setter;
import mk.ukim.finki.sharedkernel.domain.base.AbstractEntity;
import mk.ukim.finki.sharedkernel.domain.role.RoleName;
import mk.ukim.finki.usermanagement.domain.value.RoleId;


import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "roles")
public class Role extends AbstractEntity<RoleId> {

    public Role() {
        super(new RoleId());
    }

    @Column(name = "role_name", nullable = false)
    private RoleName roleName;

    @OneToMany(
            targetEntity = User.class,
            mappedBy = "role",
            fetch = FetchType.LAZY
    )
    private Set<User> users;

}
