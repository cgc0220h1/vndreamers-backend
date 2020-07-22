package com.codegym.vndreamers.apis;

import com.codegym.vndreamers.dtos.JWTResponse;
import com.codegym.vndreamers.exceptions.DatabaseException;
import com.codegym.vndreamers.exceptions.UserExistException;
import com.codegym.vndreamers.models.User;
import com.codegym.vndreamers.services.auth.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.xml.bind.ValidationException;

@CrossOrigin("*")
@RestController
@RequestMapping(
        value = "/auth",
        produces = MediaType.APPLICATION_JSON_VALUE,
        consumes = MediaType.APPLICATION_JSON_VALUE
)
public class AuthAPI {

    private AuthService authService;

    @Autowired
    public void setAuthService(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping(value = "/register")
    public JWTResponse registerUser(@RequestBody @Valid User user) throws ValidationException, DatabaseException, UserExistException {
        if (!user.getPassword().equals(user.getConfirmPassword())) {
            throw new ValidationException("password not match");
        }
        return authService.register(user);
    }

    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleValidationException() {
        return "{\"error\":\"Invalid Request Body\"}";
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public String handleConflictException() {
        return "{\"error\":\"Data Integrity Violation!\"}";
    }

    @ExceptionHandler(UserExistException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public String handleExistUserException() {
        return "{\"error\":\"User Existed!\"}";
    }
}
