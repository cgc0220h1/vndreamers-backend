package com.codegym.vndreamers.services.role;

import com.codegym.vndreamers.enums.RoleName;
import com.codegym.vndreamers.models.Role;
import com.codegym.vndreamers.services.GenericCRUDService;

import javax.persistence.EntityNotFoundException;
import java.util.Set;

public interface RoleService extends GenericCRUDService<Role> {
    Set<Role> getRolesByUserId(int userId);

    Role findRoleByEnum(RoleName roleName) throws EntityNotFoundException;
}
