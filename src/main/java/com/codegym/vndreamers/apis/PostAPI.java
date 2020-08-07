package com.codegym.vndreamers.apis;

import com.codegym.vndreamers.exceptions.EntityExistException;
import com.codegym.vndreamers.exceptions.PostDeleteException;
import com.codegym.vndreamers.exceptions.PostNotFoundException;
import com.codegym.vndreamers.models.Comment;
import com.codegym.vndreamers.models.Post;
import com.codegym.vndreamers.models.PostReaction;
import com.codegym.vndreamers.models.User;
import com.codegym.vndreamers.services.comment.CommentService;
import com.codegym.vndreamers.services.post.PostCRUDService;
import com.codegym.vndreamers.services.reaction.ReactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Collections;
import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping(
        value = "/api",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
)
public class PostAPI {

    private CommentService commentService;

    private PostCRUDService postCRUDService;

    private ReactionService reactionService;

    @Autowired
    public PostAPI(CommentService commentService, PostCRUDService postCRUDService, ReactionService reactionService) {
        this.commentService = commentService;
        this.postCRUDService = postCRUDService;
        this.reactionService = reactionService;
    }

    @GetMapping("/posts/{id}/comments")
    public List<Comment> listAllComments(@PathVariable("id") int id) {
        Post post = postCRUDService.findById(id);
        Iterable<Comment> comments = commentService.findAllByPost(post);
        return (List<Comment>) comments;
    }

    @PostMapping("/posts")
    public Post savePost(@RequestBody @Valid Post post) throws SQLIntegrityConstraintViolationException, EntityExistException {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        post.setUser(user);
        Post post1 = postCRUDService.save(post);
        return post1;
    }

    @GetMapping("/admin/posts")
    public List<Post> getAllPostsUser() throws PostNotFoundException {
        List<Post> posts = postCRUDService.findAll();
        for (Post post : posts) {
            List<PostReaction> postReaction = reactionService.getAllReactionByPostId(post.getId());
            int likes = postReaction.size();
            post.setLikeQuantity(likes);
        }
        if (posts != null) {
            Collections.reverse(posts);
            return posts;
        } else {
            throw new PostNotFoundException();
        }
    }

    @DeleteMapping("/posts/{id}")
    public Post deletePostsUser(@PathVariable("id") int id) throws PostDeleteException {
        Post post = postCRUDService.findById(id);
        if (post == null || post.getStatus() == 0) {
            throw new PostDeleteException();
        }
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Post deletedPost = postCRUDService.deletePostByIdAndUserId(Integer.valueOf(id), Integer.valueOf(user.getId()));
        if (deletedPost != null) {
            return deletedPost;
        } else {
            throw new PostDeleteException();
        }
    }

    @PutMapping("/posts")
    public Post updatePost(@RequestBody Post post) throws SQLIntegrityConstraintViolationException, EntityExistException, PostNotFoundException {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Post currentPost = postCRUDService.findById(user.getId());
        if (currentPost == null) {
            throw new PostNotFoundException();
        }
        post.setUser(user);
        postCRUDService.save(post);
        return post;
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
