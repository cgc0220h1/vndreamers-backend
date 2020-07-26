package com.codegym.vndreamers.repositories;

import com.codegym.vndreamers.models.PostReaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReactionRepository extends JpaRepository<PostReaction, Integer> {
    List<PostReaction> findAllByPostId(Integer id);
    PostReaction deleteByPostIdAndUserId(Integer postId, Integer userId);
}
