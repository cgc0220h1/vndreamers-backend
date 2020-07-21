package com.codegym.vndreamers.apis;

import com.codegym.vndreamers.models.User;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(
        value = "/auth",
        produces = MediaType.APPLICATION_JSON_VALUE,
        consumes = MediaType.APPLICATION_JSON_VALUE
)
public class AuthAPI {

    @PostMapping(value = "/register")
    public User registerUser() {
        return null;
    }

}
