package com.codegym.vndreamers.services.friendrequest;

import com.codegym.vndreamers.exceptions.EntityExistException;
import com.codegym.vndreamers.models.FriendRequest;
import com.codegym.vndreamers.repositories.FriendRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

@Service
public class FriendRequestServiceImp implements FriendRequestService {

    @Autowired
    private FriendRequestRepository friendRequestRepository;

    @Override
    public List<FriendRequest> findAll() {
        return null;
    }

    @Override
    public List<FriendRequest> findAll(Sort sort) {
        return null;
    }

    @Override
    public Page<FriendRequest> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public FriendRequest findById(int id) {
        return null;
    }

    @Override
    public FriendRequest save(FriendRequest model) throws SQLIntegrityConstraintViolationException, EntityExistException {
        return friendRequestRepository.save(model);
    }

    @Override
    public FriendRequest update(FriendRequest model) {
        return null;
    }

    @Override
    public boolean delete(int id) {
        FriendRequest friendRequest = friendRequestRepository.findById(id).get();
        if (friendRequest != null) {
            friendRequestRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public FriendRequest getFriendRequestByUserSensIdAndUserReceiveId(Integer userSendId, Integer userReceiveId) {
        return friendRequestRepository.findByUserSendIdAndUserReceiveId(userSendId, userReceiveId);
    }


    @Override
    public List<FriendRequest> getAllFriendRequestByUserIdAndByStatus(Integer userId, int status) {
        return friendRequestRepository.findAllByUserSendIdOrUserReceiveIdAndStatus(userId, userId, status);
    }

    @Override
    public List<FriendRequest> getAllFriendRequestToMeByUserIdAndByStatus(Integer userId, int status) {
        return friendRequestRepository.findAllByUserReceiveIdAndStatus(userId, status);
    }

    @Override
    public List<FriendRequest> getAllFriendRequestFromMeByUserIdAndByStatus(Integer userId, int status) {
        return friendRequestRepository.findAllByUserSendIdAndStatus(userId, status);
    }

    @Override
    public boolean isFriend(Integer userSendId1, Integer userReceiveId2, int status) {
        FriendRequest friendRequest = friendRequestRepository.findByUserSendIdAndUserReceiveIdAndStatus(userSendId1, userReceiveId2, status);
        FriendRequest friendRequestReverse = friendRequestRepository.findByUserSendIdAndUserReceiveIdAndStatus(userReceiveId2, userSendId1, status);
        if (friendRequest != null || friendRequestReverse != null){
            return true;
        }else {
            return false;
        }
    }


}
