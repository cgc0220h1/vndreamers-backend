package com.codegym.vndreamers.repositories;

import com.codegym.vndreamers.models.User;
import org.springframework.data.repository.PagingAndSortingRepository;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends PagingAndSortingRepository<User, Integer> {
    Optional<User> findByUsername(String username);

    List<User> findAllByCreatedDateAfter(Timestamp timestamp);

    Optional<User> findByEmail(@Email @NotNull String email);

    Iterable<User> findAllByCreatedDateBetween(Timestamp createdDateBegin, Timestamp createdDateEnd);
}
