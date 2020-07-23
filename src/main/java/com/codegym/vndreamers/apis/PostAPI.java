package com.codegym.vndreamers.apis;

import com.codegym.vndreamers.exceptions.EntityExistException;
import com.codegym.vndreamers.exceptions.PostDeleteException;
import com.codegym.vndreamers.exceptions.PostNotFoundException;
import com.codegym.vndreamers.models.Post;
import com.codegym.vndreamers.models.User;
import com.codegym.vndreamers.services.post.PostCRUDService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin("*")
public class PostAPI {

    @Autowired
    private PostCRUDService postCRUDService;

    @PostMapping("/posts")
    public Post savePost(@RequestBody @Valid Post post) throws SQLIntegrityConstraintViolationException, EntityExistException {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        post.setUser(user);
        Post post1 = postCRUDService.save(post);
        return post1;
    }

    @GetMapping("/posts")
    public List<Post> getAllPostsUser() throws PostNotFoundException {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Post> posts = postCRUDService.getAllByUserIdAndStatus(Integer.valueOf(user.getId()), 1);
        if (posts != null) {
            return posts;
        } else {
            throw new PostNotFoundException();
        }
    }

    @DeleteMapping("/posts/{id}")
    public String deletePostsUser(@PathVariable("id") int id) throws PostDeleteException {
        try {
            Post post = postCRUDService.findById(id);
            if (post.getStatus() == 0) {
                throw new PostDeleteException();
            }
        } catch (Exception e) {
            throw new PostDeleteException();
        }
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        boolean isRemoved = postCRUDService.deletePostByIdAndUserId(Integer.valueOf(id), Integer.valueOf(user.getId()));
        if (isRemoved) {
            return "Delete successfully";
        } else {
            throw new PostDeleteException();
        }
    }

    @ExceptionHandler(PostNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handlePostNotFoundException() {
        return "{\"error\":\"Post not found!\"}";
    }

    @ExceptionHandler(EntityExistException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public String handleExistPostException() {
        return "{\"error\":\"Post Existed!\"}";
    }

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public String handleConstraintException() {
        return "{\"error\":\"Post have constrain exception!\"}";
    }

    @ExceptionHandler(PostDeleteException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleDeleteException() {
        return "{\"error\":\"Post delete exception!\"}";
    }
}
