package com.codegym.vndreamers.services.role;

import com.codegym.vndreamers.enums.EnumRole;
import com.codegym.vndreamers.exceptions.EntityExistException;
import com.codegym.vndreamers.models.Role;
import com.codegym.vndreamers.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import java.util.Set;

@Service
public class RoleServiceIml implements RoleService {

    private final RoleRepository roleRepository;

    @Autowired
    public RoleServiceIml(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

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
    public Role findById(int id) throws EntityNotFoundException {
        return null;
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
        roleRepository.deleteById(id);
        return false;
    }

    @Override
    public Set<Role> getRolesByUserId(int userId) {
        return roleRepository.getRolesByUserId(userId);
    }

    @Override
    public Role findRoleByEnum(EnumRole enumRole) {
        return roleRepository.findByEnumRole(enumRole).orElseThrow(EntityNotFoundException::new);
    }
}
