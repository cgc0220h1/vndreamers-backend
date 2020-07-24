package com.codegym.vndreamers.repositories;

import com.codegym.vndreamers.models.Comment;
import com.codegym.vndreamers.models.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface CommentRepository extends PagingAndSortingRepository<Comment, Integer> {
    List<Comment> findAllByPostAndStatus(Post post, int status);
    Comment findByIdAndStatus(int id, int status);
}
