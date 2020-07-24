package com.codegym.vndreamers.apis;

import com.codegym.vndreamers.exceptions.EntityExistException;
import com.codegym.vndreamers.models.Comment;
import com.codegym.vndreamers.models.Post;
import com.codegym.vndreamers.models.User;
import com.codegym.vndreamers.services.comment.CommentService;
import com.codegym.vndreamers.services.post.PostCRUDService;
import com.codegym.vndreamers.services.user.UserCRUDService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping(
        value = "/auth",
        produces = MediaType.APPLICATION_JSON_VALUE,
        consumes = MediaType.APPLICATION_JSON_VALUE
)
//@PropertySource("classpath:config/status.properties")
public class CommentAPI {
    @Autowired
    private PostCRUDService postCRUDService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private UserCRUDService userCRUDService;

    @PostMapping (value = "/{id}/comments")
    public Comment createComment(@RequestBody Comment model, @PathVariable("id") int id, UriComponentsBuilder ucBuilder) throws SQLIntegrityConstraintViolationException, EntityExistException {
        Post post = postCRUDService.findById(id);
        User user = userCRUDService.findById(id);
        model.setPost(post);
        model.setUser(user);
         return commentService.save(model);
    }
//    @GetMapping (value = "/{id}/comments")
//    public List<Comment> listAllComments(@PathVariable("id") int id) {
//        Post post = postCRUDService.findById(id);
//        return commentService.findAllExistByPost(post);
//
////        List<Comment> comments = commentService.findAllExistByPost(post, id);
////        return new ResponseEntity<List<Comment>>(comments, HttpStatus.OK);
//    }
}
