package com.codegym.vndreamers.services.auth;

import com.codegym.vndreamers.dtos.JWTResponse;
import com.codegym.vndreamers.dtos.LoginRequest;
import com.codegym.vndreamers.exceptions.DatabaseException;
import com.codegym.vndreamers.exceptions.EntityExistException;
import com.codegym.vndreamers.models.Role;
import com.codegym.vndreamers.models.User;
import com.codegym.vndreamers.services.GenericCRUDService;
import com.codegym.vndreamers.services.auth.jwt.JWTIssuer;
import com.codegym.vndreamers.services.role.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Collection;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class AuthServiceImp implements AuthService {
    private final JWTIssuer jwtIssuer;

    private final GenericCRUDService<User> userService;

    private final AuthenticationManager authenticationManager;

    private final RoleService roleService;

    @Autowired
    public AuthServiceImp(JWTIssuer jwtIssuer, GenericCRUDService<User> userService, AuthenticationManager authenticationManager, RoleService roleService) {
        this.jwtIssuer = jwtIssuer;
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.roleService = roleService;
    }

    @Override
    public JWTResponse authenticate(LoginRequest loginRequest) {
        JWTResponse jwtResponse = new JWTResponse();
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );
        if (authentication.isAuthenticated()) {
            User userVerified = (User) authentication.getPrincipal();
            String token = jwtIssuer.generateToken(userVerified);
            Set<Role> roles = roleService.getRolesByUserId(userVerified.getId());
            jwtResponse.setRoles(roles);
            jwtResponse.setAccessToken(token);
            jwtResponse.setUser(userVerified);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        return jwtResponse;
    }

    @Override
    public User register(@Valid User user) throws DatabaseException, EntityExistException {
        User userRegistered;


        try {
            userRegistered = saveUserToDB(user);
        } catch (SQLIntegrityConstraintViolationException exception) {
            throw new DatabaseException();
        }
        return userRegistered;
    }

    private User saveUserToDB(User user) throws SQLIntegrityConstraintViolationException, EntityExistException {
        User userSaved;
        String username = getUsernameFromEmail(user.getEmail());
        user.setUsername(username);
        userSaved = userService.save(user);
        return userSaved;
    }

    private String getUsernameFromEmail(String email) throws IndexOutOfBoundsException {
        Pattern pattern = Pattern.compile("@");
        Matcher matcher = pattern.matcher(email);
        int indexOfMatcher = matcher.find() ? matcher.start() : -1;
        return email.substring(0, indexOfMatcher);
    }
}
