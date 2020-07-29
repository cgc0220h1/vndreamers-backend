package com.codegym.vndreamers.services.comment;

import com.codegym.vndreamers.models.Comment;
import com.codegym.vndreamers.models.Post;
import com.codegym.vndreamers.services.GenericCRUDService;

import java.util.List;
import java.util.Optional;

public interface CommentService extends GenericCRUDService<Comment> {

    Iterable<Comment> findAllByPost(Post post);
    List<Comment> findAllByPostId(Integer postId);
    void removeComment(Integer id);

    List<Comment> findAllCommentByUserId (Integer id);

}
