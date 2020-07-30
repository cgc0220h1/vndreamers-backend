package com.codegym.vndreamers.services.admin;

import com.codegym.vndreamers.models.User;
import com.codegym.vndreamers.repositories.UserRepository;
import com.codegym.vndreamers.services.AdminStatisticService;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.LinkedList;
import java.util.List;

@Service
public class AdminStatisticServiceImp implements AdminStatisticService {
    private UserRepository userRepository;

    public AdminStatisticServiceImp(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<User> getUsersRegisterToday() {
        Timestamp startOfDay = Timestamp.valueOf(LocalDate.now().atStartOfDay());
        Timestamp endOfDay = Timestamp.valueOf(LocalDate.now().atTime(LocalTime.MAX));
        List<User> userList = new LinkedList<>();
        Iterable<User> iterable = userRepository.findAllByCreatedDateBetween(startOfDay, endOfDay);
        for (User user : iterable) {
            userList.add(user);
        }
        return userList;
    }
}
