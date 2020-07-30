package com.codegym.vndreamers.apis;

import com.codegym.vndreamers.exceptions.EntityExistException;
import com.codegym.vndreamers.exceptions.UserDeleteException;
import com.codegym.vndreamers.models.User;
import com.codegym.vndreamers.services.user.UserCRUDService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Timestamp;
import java.util.List;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin("*")
public class AdminAPI {

    public static final int BLOCK_STATUS = 0;
    public static final int ACTIVE_STATUS = 1;

    @Autowired
    private UserCRUDService userCRUDService;

    @GetMapping("/users/date/{quantity}")
    public int getAllByDate(@PathVariable("quantity") long date) {
        long currentTime = System.currentTimeMillis();
        long currentTimeWant = date * 24 * 60 * 60 * 1000;
        long timeWant = currentTime - currentTimeWant;
        Timestamp dateWant = new Timestamp(timeWant);
        List<User> users = userCRUDService.getAllUserByTimeStamp(dateWant);
        int quantity = users.size();
        return quantity;
    }



    @GetMapping("/users")
    public List<User> getAllUser() {
        return userCRUDService.findAll();
    }

    @GetMapping("/users/{id}")
    public User getUserById(@PathVariable int id) {
        return userCRUDService.findById(id);
    }

    @DeleteMapping("/users/{id}")
    public User deleteUserById(@PathVariable int id) throws UserDeleteException {
        User user = userCRUDService.findById(id);
        if (user != null) {
            try {
                boolean isDeleted = userCRUDService.delete(id);
                if (isDeleted) {
                    return user;
                } else {
                    return null;
                }
            }catch (Exception e){
                throw new UserDeleteException();
            }
        }
        return null;
    }

    @PutMapping("/users/status")
    public User activeUserById(@RequestBody User user) throws SQLIntegrityConstraintViolationException, EntityExistException {
        User userFound = userCRUDService.findById(user.getId());
        userFound.setStatus(user.getStatus());
        userFound.setConfirmPassword(userFound.getPassword());
        return userCRUDService.updateProfileUser(userFound);
    }

    @ExceptionHandler(UserDeleteException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public String handleDeleteException() {
        return "{\"error\":\"USer delete exception! foreign key constraints\"}";
    }
}

