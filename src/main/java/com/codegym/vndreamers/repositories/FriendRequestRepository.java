package com.codegym.vndreamers.repositories;


import com.codegym.vndreamers.models.FriendRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FriendRequestRepository extends JpaRepository<FriendRequest, Integer> {
    FriendRequest findByUserSendIdAndUserReceiveId(Integer userSendId, Integer userReceiveId);

}
