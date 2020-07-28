package com.codegym.vndreamers.repositories;

import com.codegym.vndreamers.models.Role;
import com.codegym.vndreamers.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Set;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Set<Role> findAllByUsers(Set<User> users);
}
