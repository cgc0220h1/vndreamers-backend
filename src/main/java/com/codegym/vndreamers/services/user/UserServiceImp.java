package com.codegym.vndreamers.services.user;

import com.codegym.vndreamers.exceptions.EntityExistException;
import com.codegym.vndreamers.models.User;
import com.codegym.vndreamers.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImp implements UserCRUDService, UserDetailsService {
    private final UserRepository userRepository;

    @Autowired
    public UserServiceImp(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<User> findAll() {
        return null;
    }

    @Override
    public List<User> findAll(Sort sort) {
        return null;
    }

    @Override
    public Page<User> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public User findById(int id) {
     return userRepository.findById(id).get();
    }

    @Override
    public User save(User user) throws SQLIntegrityConstraintViolationException, EntityExistException {
        User userFound = (User) loadUserByUsername(user.getEmail());
        if (userFound.getEmail() != null) {
            throw new EntityExistException();
        }
        if (user.getUsername() == null) {
            throw new SQLIntegrityConstraintViolationException();
        }
        return userRepository.save(user);
    }

    @Override
    public User update(User model) {
        return null;
    }

    @Override
    public boolean delete(int id) {
        return false;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username).orElse(new User());
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username).get();
    }

    @Override
    public User updateProfileUser(User user) {
        return userRepository.save(user);
    }
}
