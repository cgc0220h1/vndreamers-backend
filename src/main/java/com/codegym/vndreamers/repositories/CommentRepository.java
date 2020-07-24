package com.codegym.vndreamers.repositories;

import com.codegym.vndreamers.models.Comment;
import com.codegym.vndreamers.models.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface CommentRepository extends PagingAndSortingRepository<Comment, Integer> {
    List<Comment> findAllByPost(Post post);

    Page<Comment> findAllByPost(Post post, Pageable pageable);

    Comment findById(int id);
}
