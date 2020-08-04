package com.codegym.vndreamers.apis;

import com.codegym.vndreamers.exceptions.EntityExistException;
import com.codegym.vndreamers.exceptions.ReactionExistException;
import com.codegym.vndreamers.models.Post;
import com.codegym.vndreamers.models.PostReaction;
import com.codegym.vndreamers.models.User;
import com.codegym.vndreamers.services.post.PostCRUDService;
import com.codegym.vndreamers.services.reaction.ReactionService;
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

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

@RestController
@RequestMapping(value = "/api")
@CrossOrigin("*")
public class ReactionAPI {

    private PostCRUDService postCRUDService;

    private ReactionService reactionService;

    @Autowired
    public ReactionAPI(PostCRUDService postCRUDService, ReactionService reactionService) {
        this.postCRUDService = postCRUDService;
        this.reactionService = reactionService;
    }

    @PostMapping("/posts/{postId}/reactions")
    public PostReaction createReactionPost(@PathVariable int postId, @RequestBody PostReaction postReaction) throws SQLIntegrityConstraintViolationException, EntityExistException, ReactionExistException {
        Post post = postCRUDService.findById(postId);
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        PostReaction postReaction1 = reactionService.findByPostAndUser(post, user);
        if (postReaction1 != null) {
            throw new ReactionExistException();
        }
        postReaction.setPost(post);
        postReaction.setUser(user);
        reactionService.save(postReaction);
        return postReaction;
    }

    @GetMapping("/posts/{postId}/reactions")
    public List<PostReaction> getReactionsPost(@PathVariable int postId) {
        Post post = postCRUDService.findById(postId);
        if (post != null) {
            return reactionService.getAllReactionByPostId(postId);
        } else {
            return null;
        }
    }

    @DeleteMapping("/posts/{postId}/reactions")
    public PostReaction deleteReactionsPost(@PathVariable int postId) {
        Post post = postCRUDService.findById(postId);
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        PostReaction postReaction = reactionService.findByPostAndUser(post, user);
        if (post != null) {
            reactionService.delete(postReaction.getId());
//            reactionService.deleteByPostAndUser(post, user);
            return postReaction ;
        } else {
            return null;
        }
    }

    @ExceptionHandler(ReactionExistException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public String handleException() {
        return "{\"error\":\"reaction exist\"}";
    }
}
