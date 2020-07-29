package com.codegym.vndreamers.services.friendrequest;

import com.codegym.vndreamers.models.FriendRequest;
import com.codegym.vndreamers.services.GenericCRUDService;

import java.util.List;

public interface FriendRequestService extends GenericCRUDService<FriendRequest> {
    FriendRequest getFriendRequestByUserSensIdAndUserReceiveId(Integer userSendId, Integer userReceiveId);

    List<FriendRequest> getAllFriendRequestByUserIdAndByStatus(Integer userId, int status);

    List<FriendRequest> getAllFriendRequestUserReceived(Integer userId, int status);

    List<FriendRequest> getAllFriendRequestSentByUser(Integer userId, int status);
    List<FriendRequest> getAllFriendByUserId(Integer userSendId, Integer status1, Integer userReceiveId, Integer status2);

    boolean isFriend(Integer userSendId1, Integer userReceiveId2, int status);

}
