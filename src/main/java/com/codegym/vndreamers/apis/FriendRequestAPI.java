package com.codegym.vndreamers.apis;

import com.codegym.vndreamers.exceptions.EntityExistException;
import com.codegym.vndreamers.models.FriendRequest;
import com.codegym.vndreamers.models.User;
import com.codegym.vndreamers.services.friendrequest.FriendRequestService;
import com.codegym.vndreamers.services.user.UserCRUDService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/api")
@CrossOrigin("*")
public class FriendRequestAPI {

    public static final int NO_FRIEND_STATUS = 0;
    public static final int FRIEND_STATUS = 1;
    public static final int BLOCK_STATUS = 2;

    @Autowired
    private UserCRUDService userCRUDService;

    @Autowired
    private FriendRequestService friendRequestService;

    @PostMapping("/friends/{receiveId}")
    public FriendRequest SendFriendRequest(@PathVariable int receiveId) throws SQLIntegrityConstraintViolationException, EntityExistException {
        User userSend = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User userReceive = userCRUDService.findById(receiveId);
        FriendRequest isNullFriendRequest = friendRequestService.getFriendRequestByUserSensIdAndUserReceiveId(userSend.getId(), receiveId);
        FriendRequest isNullReverseFriendRequest = friendRequestService.getFriendRequestByUserSensIdAndUserReceiveId(receiveId, userSend.getId());
        if (isNullFriendRequest == null && isNullReverseFriendRequest == null && userSend.getId() != receiveId){
            FriendRequest friendRequest = new FriendRequest();
            friendRequest.setUserSend(userSend);
            friendRequest.setUserReceive(userReceive);
            friendRequest.setStatus(NO_FRIEND_STATUS);
            return friendRequestService.save(friendRequest);
        }else {
            return null;
        }
    }

    @PutMapping("/friends/{userSendId}")
    public FriendRequest ConfirmFriendRequest(@PathVariable int userSendId) throws SQLIntegrityConstraintViolationException, EntityExistException {
        User userConfirm = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        FriendRequest friendRequest = friendRequestService.getFriendRequestByUserSensIdAndUserReceiveId(userSendId, userConfirm.getId());
        if (friendRequest != null){
            friendRequest.setStatus(FRIEND_STATUS);
            return friendRequestService.save(friendRequest);
        }else {
            return null ;
        }
    }

    @DeleteMapping("/friends/{userOtherId}")
    public FriendRequest deleteFriendRequestOrDeleteFriend(@PathVariable int userOtherId ){
        User myUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        FriendRequest isNullFriendRequest = friendRequestService.getFriendRequestByUserSensIdAndUserReceiveId(myUser.getId(), userOtherId);
        FriendRequest isNullReverseFriendRequest = friendRequestService.getFriendRequestByUserSensIdAndUserReceiveId(userOtherId, myUser.getId());
        if (isNullFriendRequest != null){
            friendRequestService.delete(isNullFriendRequest.getId());
            return isNullFriendRequest;
        }else if (isNullReverseFriendRequest != null){
            friendRequestService.delete(isNullReverseFriendRequest.getId());
            return isNullReverseFriendRequest;
        }else {
            return null;
        }
    }

    @GetMapping("/friends/{userId}")
    public List<User> getAllFriend(@PathVariable int userId){
        List<FriendRequest> friendRequests =  friendRequestService.getAllFriendRequestByUserIdAndByStatus(userId, FRIEND_STATUS);
        List<User> userList = new ArrayList<>();
        for (FriendRequest friendRequest : friendRequests){
            if (friendRequest.getUserSend().getId() != userId){
                userList.add(friendRequest.getUserSend());
            }else {
                userList.add(friendRequest.getUserReceive());
            }
        }
        return userList;
    }

    @GetMapping("/friend-requests-to-me")
    public List<User> getFriendRequestsToMe(){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<User> userList = new ArrayList<>();
        List<FriendRequest> friendRequests = friendRequestService.getAllFriendRequestToMeByUserIdAndByStatus(user.getId(), NO_FRIEND_STATUS);
        for (FriendRequest friendRequest : friendRequests){
           userList.add(friendRequest.getUserSend());
        }
        return userList;
    }


}
