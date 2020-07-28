package com.codegym.vndreamers.repositories;


import com.codegym.vndreamers.models.FriendRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FriendRequestRepository extends JpaRepository<FriendRequest, Integer> {
    FriendRequest findByUserSendIdAndUserReceiveId(Integer userSendId, Integer userReceiveId);
    List<FriendRequest> findAllByUserSendIdOrUserReceiveIdAndStatus(Integer userSendId, Integer userReceiveId, Integer status);
    List<FriendRequest> findAllByUserReceiveIdAndStatus(Integer userId, Integer status);
    List<FriendRequest> findAllByUserSendIdAndStatus(Integer userId, Integer status);

}
