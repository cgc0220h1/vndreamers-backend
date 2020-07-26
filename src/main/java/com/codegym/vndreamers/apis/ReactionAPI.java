package com.codegym.vndreamers.apis;

import com.codegym.vndreamers.exceptions.EntityExistException;
import com.codegym.vndreamers.models.Post;
import com.codegym.vndreamers.models.PostReaction;
import com.codegym.vndreamers.models.User;
import com.codegym.vndreamers.repositories.ReactionRepository;
import com.codegym.vndreamers.services.post.PostCRUDService;
import com.codegym.vndreamers.services.reaction.ReactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/api")
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
