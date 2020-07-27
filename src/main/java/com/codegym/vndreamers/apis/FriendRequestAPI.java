package com.codegym.vndreamers.apis;

import com.codegym.vndreamers.exceptions.EntityExistException;
import com.codegym.vndreamers.models.FriendRequest;
import com.codegym.vndreamers.models.User;
import com.codegym.vndreamers.services.friendrequest.FriendRequestService;
import com.codegym.vndreamers.services.user.UserCRUDService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLIntegrityConstraintViolationException;


@RestController
@RequestMapping("/api")
@CrossOrigin("*")
public class FriendRequestAPI {

    @Autowired
    private UserCRUDService userCRUDService;

    @Autowired
    private FriendRequestService friendRequestService;

    @PostMapping("/friends/{userId}")
    public FriendRequest SendFriendRequest(@PathVariable int userId) throws SQLIntegrityConstraintViolationException, EntityExistException {
        User userSend = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User userReceive = userCRUDService.findById(userId);
        FriendRequest friendRequest = new FriendRequest();
        friendRequest.setUserSend(userSend);
        friendRequest.setUserReceive(userReceive);
        return friendRequestService.save(friendRequest);

    }

    @PutMapping("/friends/{userSendId}")
    public FriendRequest ConfirmFriendRequest(@PathVariable int userSendId) throws SQLIntegrityConstraintViolationException, EntityExistException {
        User userConfirm = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        FriendRequest friendRequest = friendRequestService.getFriendRequestByUserSensIdAndUserReceiveId(userSendId, userConfirm.getId());
        friendRequest.setStatus(1);
        return friendRequestService.save(friendRequest);

    }

}
