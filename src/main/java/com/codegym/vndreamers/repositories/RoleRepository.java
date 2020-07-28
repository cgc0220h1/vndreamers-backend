package com.codegym.vndreamers.repositories;

import com.codegym.vndreamers.models.Role;
import com.codegym.vndreamers.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Set<Role> findAllByUserI
}
