package com.codegym.vndreamers.apis;

import com.codegym.vndreamers.dtos.RangeRequest;
import com.codegym.vndreamers.exceptions.UserDeleteException;
import com.codegym.vndreamers.models.User;
import com.codegym.vndreamers.services.AdminStatisticService;
import com.codegym.vndreamers.services.user.UserCRUDService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityNotFoundException;
import java.sql.Timestamp;
import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping(
        value = "/api/admin",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
)
public class AdminAPI {
    private final AdminStatisticService adminStatisticService;
    private final UserCRUDService userCRUDService;

    @Autowired
    public AdminAPI(AdminStatisticService adminStatisticService, UserCRUDService userCRUDService) {
        this.adminStatisticService = adminStatisticService;
        this.userCRUDService = userCRUDService;
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
        if (user == null) {
            throw new EntityNotFoundException();
        }
        boolean isDeleted = userCRUDService.delete(id);
        if (!isDeleted) {
            throw new UserDeleteException();
        }
        return user;
    }

    @PutMapping("/users/status")
    public User activeUserById(@RequestBody User user) {
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

