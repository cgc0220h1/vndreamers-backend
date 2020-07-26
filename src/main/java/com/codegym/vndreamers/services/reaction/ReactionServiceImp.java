package com.codegym.vndreamers.services.reaction;

import com.codegym.vndreamers.exceptions.EntityExistException;
import com.codegym.vndreamers.models.PostReaction;
import com.codegym.vndreamers.repositories.ReactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

@Service
public class ReactionServiceImp implements ReactionService {

    @Autowired
    private ReactionRepository reactionRepository;

    @Override
    public List<PostReaction> findAll() {
        return reactionRepository.findAll();
    }

    @Override
    public List<PostReaction> findAll(Sort sort) {
        return null;
    }

    @Override
    public Page<PostReaction> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public PostReaction findById(int id) {
        return null;
    }

    @Override
    public PostReaction save(PostReaction model) throws SQLIntegrityConstraintViolationException, EntityExistException {
        return reactionRepository.save(model);
    }

    @Override
    public PostReaction update(PostReaction model) {
        return null;
    }

    @Override
    public boolean delete(int id) {
        reactionRepository.deleteById(id);
        return true;
    }

    @Override
    public List<PostReaction> getAllReactionByPostId(Integer id) {
        return reactionRepository.findAllByPostId(id);
    }

    @Override
    public PostReaction deleteByPostIdAndUserId(Integer postId, Integer userId) {
        return reactionRepository.deleteByPostIdAndUserId(postId, userId);
    }
}
