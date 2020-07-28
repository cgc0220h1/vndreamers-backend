package com.codegym.vndreamers.services.role;

import com.codegym.vndreamers.models.Role;
import com.codegym.vndreamers.models.User;
import com.codegym.vndreamers.services.GenericCRUDService;

import java.util.Set;

public interface RoleService extends GenericCRUDService<Role> {
    Set<Role> getAllByUser(User user);
}
