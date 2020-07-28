package com.codegym.vndreamers.repositories;

import com.codegym.vndreamers.models.Comment;
import com.codegym.vndreamers.models.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends PagingAndSortingRepository<Comment, Integer> {
    List<Comment> findAllByPost(Post post);
    List<Comment> findAllByPostId(Integer postId);

    Page<Comment> findAllByPost(Post post, Pageable pageable);

}
