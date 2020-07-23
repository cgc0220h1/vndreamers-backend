package com.codegym.vndreamers.repositories;

import com.codegym.vndreamers.models.User;
import org.springframework.data.repository.PagingAndSortingRepository;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.util.Optional;

public interface UserRepository extends PagingAndSortingRepository<User, Integer> {
    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(@Email @NotNull String email);
}
