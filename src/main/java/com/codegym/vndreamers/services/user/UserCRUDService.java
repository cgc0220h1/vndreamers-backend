package com.codegym.vndreamers.services.user;

import com.codegym.vndreamers.models.User;
import com.codegym.vndreamers.services.GenericCRUDService;

import java.sql.Timestamp;
import java.util.List;

public interface UserCRUDService extends GenericCRUDService<User> {
    User findByUsername(String username);
    User updateProfileUser(User user);
    List<User> getAllUserByTimeStamp(Timestamp timestamp);
}
