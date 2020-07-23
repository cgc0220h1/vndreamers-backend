package com.codegym.vndreamers.services.user;

import com.codegym.vndreamers.models.User;
import com.codegym.vndreamers.services.GenericCRUDService;

public interface UserCRUDService extends GenericCRUDService<User> {
    User findByUsername(String username);
}
