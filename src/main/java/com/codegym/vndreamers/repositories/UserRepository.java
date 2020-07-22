package com.codegym.vndreamers.repositories;

import com.codegym.vndreamers.models.User;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface UserRepository extends PagingAndSortingRepository<User, Integer> {
}
