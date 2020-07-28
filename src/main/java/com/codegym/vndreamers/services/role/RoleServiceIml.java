package com.codegym.vndreamers.services.role;

import com.codegym.vndreamers.exceptions.EntityExistException;
import com.codegym.vndreamers.models.Role;
import com.codegym.vndreamers.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import java.util.Optional;

@Service
public class RoleServiceIml implements RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public List<Role> findAll() {
        return roleRepository.findAll();
    }

    @Override
    public List<Role> findAll(Sort sort) {
        return roleRepository.findAll(sort);
    }

    @Override
    public Page<Role> findAll(Pageable pageable) {
        return roleRepository.findAll(pageable);
    }

    @Override
    public Role findById(int id) {
        return roleRepository.findById(id).get();
    }

    @Override
    public Role save(Role model) throws SQLIntegrityConstraintViolationException, EntityExistException {
        return roleRepository.save(model);
    }

    @Override
    public Role update(Role model) {
        return roleRepository.save(model);
    }

    @Override
    public boolean delete(int id) {
        return false;
    }
}
