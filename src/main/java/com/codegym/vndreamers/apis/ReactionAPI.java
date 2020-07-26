package com.codegym.vndreamers.apis;

import com.codegym.vndreamers.exceptions.EntityExistException;
import com.codegym.vndreamers.models.Post;
import com.codegym.vndreamers.models.PostReaction;
import com.codegym.vndreamers.models.User;
import com.codegym.vndreamers.services.post.PostCRUDService;
import com.codegym.vndreamers.services.reaction.ReactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLIntegrityConstraintViolationException;

@RestController
@RequestMapping(
        value = "/api",
        produces = MediaType.APPLICATION_JSON_VALUE,
        consumes = MediaType.APPLICATION_JSON_VALUE
)
@CrossOrigin("*")
public class ReactionAPI {

    @Autowired
    private PostCRUDService postCRUDService;

    @Autowired
    private ReactionService reactionService;

    @PostMapping("/posts/{postId}/reactions")
    public PostReaction createReactionPost(@PathVariable int postId, @RequestBody PostReaction postReaction) throws SQLIntegrityConstraintViolationException, EntityExistException {
        Post post = postCRUDService.findById(postId);
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        postReaction.setPost(post);
        postReaction.setUser(user);
        reactionService.save(postReaction);
        return postReaction;
    }
}
