package mk.ukim.finki.usermanagement.domain.repository;


import mk.ukim.finki.sharedkernel.domain.role.RoleName;
import mk.ukim.finki.usermanagement.domain.model.Role;
import mk.ukim.finki.usermanagement.domain.value.RoleId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, RoleId> {

    Role findRoleByRoleName(RoleName roleName);
}
