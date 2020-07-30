package com.codegym.vndreamers.repositories;

import com.codegym.vndreamers.models.Post;
import com.codegym.vndreamers.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Integer> {
    List<Post> findAllByUserIdAndStatus(Integer userId, Integer status);
    void deleteByIdAndUserId(Integer postId, Integer userId);
    List<Post> findAllByUserIdAndStatusGreaterThanEqual(Integer userId, Integer status);
}
