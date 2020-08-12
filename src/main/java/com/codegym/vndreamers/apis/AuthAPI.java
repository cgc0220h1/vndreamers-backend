package com.codegym.vndreamers.apis;

import com.codegym.vndreamers.dtos.JWTResponse;
import com.codegym.vndreamers.dtos.LoginRequest;
import com.codegym.vndreamers.enums.RoleName;
import com.codegym.vndreamers.exceptions.DatabaseException;
import com.codegym.vndreamers.exceptions.EntityExistException;
import com.codegym.vndreamers.models.Role;
import com.codegym.vndreamers.models.User;
import com.codegym.vndreamers.services.auth.AuthService;
import com.codegym.vndreamers.services.role.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.validation.Valid;
import javax.xml.bind.ValidationException;
import java.util.HashSet;
import java.util.Set;

@CrossOrigin("*")
@RestController
@RequestMapping(
        value = "/auth",
        produces = MediaType.APPLICATION_JSON_VALUE,
        consumes = MediaType.APPLICATION_JSON_VALUE
)
public class AuthAPI {
    private final RoleService roleService;
    private final AuthService authService;

    @Autowired
    public AuthAPI(RoleService roleService, AuthService authService) {
        this.roleService = roleService;
        this.authService = authService;
    }

    @PostMapping(value = "/register")
    public User registerUser(@RequestBody @Valid User user) throws ValidationException, DatabaseException, EntityExistException {
        if (!user.getPassword().equals(user.getConfirmPassword())) {
            throw new ValidationException("password not match");
        }
        Set<Role> roles = new HashSet<>() ;
        Role role = roleService.findRoleByEnum(RoleName.USER);
        roles.add(role);
        user.setRoles(roles);
        return authService.register(user);
    }

    @PostMapping(value = "/login")
    public JWTResponse doLogin(@RequestBody @Valid LoginRequest loginRequest) throws UsernameNotFoundException {
        JWTResponse jwtResponse;
        try {
            jwtResponse = authService.authenticate(loginRequest);
            int status = jwtResponse.getUser().getStatus();
            if (status == 0) {
                throw new UsernameNotFoundException(loginRequest.getEmail());
            }
        } catch (InternalAuthenticationServiceException e) {
            throw new UsernameNotFoundException(loginRequest.getEmail());
        }
        return jwtResponse;
    }

    @ExceptionHandler({ValidationException.class,
            MethodArgumentNotValidException.class,
            MethodArgumentTypeMismatchException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleValidationException() {
        return "{\"error\":\"Invalid Request Body\"}";
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public String handleConflictException() {
        return "{\"error\":\"Data Integrity Violation!\"}";
    }

    @ExceptionHandler(EntityExistException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public String handleExistUserException() {
        return "{\"error\":\"User Existed!\"}";
    }

    @ExceptionHandler({UsernameNotFoundException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleUserNotFoundException() {
        return "{\"error\":\"User Not found!\"}";
    }

    @ExceptionHandler({BadCredentialsException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleWrongPasswordException() {
        return "{\"error\":\"Wrong Password!\"}";
    }
}

