package com.codegym.vndreamers.repositories;

import com.codegym.vndreamers.models.Post;
import com.codegym.vndreamers.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Integer> {
    List<Post> findAllByUserId(Integer id);
}
