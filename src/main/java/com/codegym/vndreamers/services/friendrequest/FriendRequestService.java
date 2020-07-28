package com.codegym.vndreamers.services.friendrequest;

import com.codegym.vndreamers.models.FriendRequest;
import com.codegym.vndreamers.services.GenericCRUDService;

import java.util.List;

public interface FriendRequestService extends GenericCRUDService<FriendRequest> {
    FriendRequest getFriendRequestByUserSensIdAndUserReceiveId(Integer userSendId, Integer userReceiveId);
    List<FriendRequest> getAllFriendRequestByUserIdAndByStatus( Integer userId, int status);
    List<FriendRequest> getAllFriendRequestToMeByUserIdAndByStatus( Integer userId, int status);
}
