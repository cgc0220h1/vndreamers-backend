package com.codegym.vndreamers.repositories;

import com.codegym.vndreamers.models.Post;
import com.codegym.vndreamers.models.PostReaction;
import com.codegym.vndreamers.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReactionRepository extends JpaRepository<PostReaction, Integer> {
    List<PostReaction> findAllByPostId(Integer id);

    void deleteByPostAndUser(Post post, User user);

    Optional<PostReaction> findByPostAndUser(Post post, User user);
//    PostReaction deleteByPostIdAndUserId(Integer postId, Integer userId);
}
