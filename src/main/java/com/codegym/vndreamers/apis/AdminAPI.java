package com.codegym.vndreamers.apis;

import com.codegym.vndreamers.dtos.RangeRequest;
import com.codegym.vndreamers.exceptions.EntityExistException;
import com.codegym.vndreamers.exceptions.UserDeleteException;
import com.codegym.vndreamers.models.User;
import com.codegym.vndreamers.services.AdminStatisticService;
import com.codegym.vndreamers.services.user.UserCRUDService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Timestamp;
import java.util.List;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin("*")
public class AdminAPI {
    private AdminStatisticService adminStatisticService;
    private UserCRUDService userCRUDService;

    public static final int BLOCK_STATUS = 0;
    public static final int ACTIVE_STATUS = 1;

    @Autowired
    public void setUserCRUDService(UserCRUDService userCRUDService) {
        this.userCRUDService = userCRUDService;
    }

    @Autowired
    public void setAdminStatisticService(AdminStatisticService adminStatisticService) {
        this.adminStatisticService = adminStatisticService;
    }

    @GetMapping("/users/date/{quantity}")
    public Object getAllByDate(@PathVariable("quantity") long date) {
        long currentTime = System.currentTimeMillis();
        long currentTimeWant = date * 24 * 60 * 60 * 1000;
        long timeWant = currentTime - currentTimeWant;
        Timestamp dateWant = new Timestamp(timeWant);
        List<User> users = userCRUDService.getAllUserByTimeStamp(dateWant);
        int quantity = users.size();
        return quantity;
    }

    @GetMapping("/users/statistics/today")
    public List<User> getUserRegisterToday() {
        return adminStatisticService.getUsersRegisterToday();
    }

    @PostMapping("/users/statistics/range")
    public List<User> getUserByRange(@RequestBody RangeRequest rangeRequest) {
        return adminStatisticService.getUserRegisterByRange(rangeRequest);
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
            } catch (Exception e) {
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

