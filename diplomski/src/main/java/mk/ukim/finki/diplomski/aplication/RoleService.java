package mk.ukim.finki.diplomski.aplication;


import mk.ukim.finki.sharedkernel.domain.role.RoleName;

import java.util.UUID;

public interface RoleService {

    UUID findRoleIdByRoleName(RoleName roleName);

    RoleName findRoleNameByRoleId(UUID roleId);
}
