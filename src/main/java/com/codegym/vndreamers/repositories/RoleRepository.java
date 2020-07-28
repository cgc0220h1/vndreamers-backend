package com.codegym.vndreamers.repositories;

import com.codegym.vndreamers.models.Role;
import com.codegym.vndreamers.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    @Query("select r from Role r join r.users user where user.id = :id")
    Set<Role> getRolesByUserId(@Param("id")int id);
}
