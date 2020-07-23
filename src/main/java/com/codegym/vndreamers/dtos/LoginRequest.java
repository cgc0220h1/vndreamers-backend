package com.codegym.vndreamers.dtos;

import com.codegym.vndreamers.models.User;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class LoginRequest {
    @Basic
    @Column(name = "email", nullable = false, length = 100, unique = true)
    @Email
    @NotNull
    private String email;

    @Basic
    @Column(name = "password", nullable = false, length = 50)
    @Size(max = 50, min = 8)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotNull
    private String password;
}
