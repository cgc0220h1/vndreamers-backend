package com.codegym.vndreamers.services.post;

import com.codegym.vndreamers.models.Post;
import com.codegym.vndreamers.models.User;
import com.codegym.vndreamers.services.GenericCRUDService;

import java.util.List;

public interface PostCRUDService extends GenericCRUDService<Post> {
    List<Post> getAllByUserIdAndStatus(Integer id, Integer status);
    boolean deletePostByIdAndUserId(Integer postId, Integer userId);
}