package com.codegym.vndreamers.services.post;

import com.codegym.vndreamers.models.Comment;
import com.codegym.vndreamers.models.Post;
import com.codegym.vndreamers.services.GenericCRUDService;

import java.util.List;
import java.util.Optional;

public interface PostCRUDService extends GenericCRUDService<Post> {
    List<Post> getAllByUserIdAndStatus(Integer id, Integer status);
    Post deletePostByIdAndUserId(Integer postId, Integer userId);
    List<Post> getAllByUserIdAndStatusGreaterThanEqual(Integer userId, Integer status);
}
