package com.codegym.vndreamers.apis;

import com.codegym.vndreamers.exceptions.EntityExistException;
import com.codegym.vndreamers.models.Post;
import com.codegym.vndreamers.models.User;
import com.codegym.vndreamers.services.post.PostCRUDService;
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

@RestController
@RequestMapping("/api")
@CrossOrigin("*")
public class ReactionAPI {

    @Autowired
    private PostCRUDService postCRUDService;

    @PostMapping("/posts/{postId}/reactions")
    public  createReactionPost(@PathVariable int postId) throws SQLIntegrityConstraintViolationException, EntityExistException {

        return post1;
    }
}
