package com.codegym.vndreamers.services.post;

import com.codegym.vndreamers.exceptions.PostNotFoundException;
import com.codegym.vndreamers.models.Comment;
import com.codegym.vndreamers.models.Post;
import com.codegym.vndreamers.services.GenericCRUDService;

import java.util.List;
import java.util.Optional;

public interface PostCRUDService extends GenericCRUDService<Post> {
    List<Post> getAllByUserIdAndStatus(Integer id, Integer status);
    Post deletePostByIdAndUserId(Integer postId, Integer userId);
    List<Post> getAllByUSerIdAndRelationShip(Integer id1, Integer status1, Integer id2, Integer status2, Integer id3, Integer status3) throws PostNotFoundException;
}
