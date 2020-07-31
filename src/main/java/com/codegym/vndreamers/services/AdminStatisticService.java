package com.codegym.vndreamers.services;

import com.codegym.vndreamers.dtos.RangeRequest;
import com.codegym.vndreamers.models.User;

import java.util.List;

public interface AdminStatisticService {
    List<User> getUsersRegisterToday();

    List<User> getUserRegisterByRange(RangeRequest rangeRequest);
}
