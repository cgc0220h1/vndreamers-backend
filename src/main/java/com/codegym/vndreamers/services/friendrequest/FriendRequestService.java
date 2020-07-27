package com.codegym.vndreamers.services.friendrequest;

import com.codegym.vndreamers.models.FriendRequest;
import com.codegym.vndreamers.services.GenericCRUDService;

public interface FriendRequestService extends GenericCRUDService<FriendRequest> {
    FriendRequest getFriendRequestByUserSensIdAndUserReceiveId(Integer userSendId, Integer userReceiveId);
}
