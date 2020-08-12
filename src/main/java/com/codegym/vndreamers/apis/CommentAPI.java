package com.codegym.vndreamers.apis;

import com.codegym.vndreamers.exceptions.CanNotUpdateCommentException;
import com.codegym.vndreamers.exceptions.CommentNotFound;
import com.codegym.vndreamers.exceptions.CanNotCommentException;
import com.codegym.vndreamers.exceptions.EntityExistException;
import com.codegym.vndreamers.models.Comment;
import com.codegym.vndreamers.models.Post;
import com.codegym.vndreamers.models.User;
import com.codegym.vndreamers.services.comment.CommentService;
import com.codegym.vndreamers.services.friendrequest.FriendRequestService;
import com.codegym.vndreamers.services.post.PostCRUDService;
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
import org.springframework.web.util.UriComponentsBuilder;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Collections;
import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping(
        value = "/api",
        produces = MediaType.APPLICATION_JSON_VALUE,
        consumes = MediaType.APPLICATION_JSON_VALUE
)
public class CommentAPI {
    public static final int FRIEND_STATUS = 1;

    private final PostCRUDService postCRUDService;

    private final CommentService commentService;

    private final FriendRequestService friendRequestService;

    @Autowired
    public CommentAPI(PostCRUDService postCRUDService, CommentService commentService, FriendRequestService friendRequestService) {
        this.postCRUDService = postCRUDService;
        this.commentService = commentService;
        this.friendRequestService = friendRequestService;
    }

    @PostMapping(value = "/posts/{postId}/comments")
    public Comment createComment(@RequestBody Comment comment, @PathVariable("postId") int id, UriComponentsBuilder ucBuilder) throws SQLIntegrityConstraintViolationException, EntityExistException, CanNotCommentException {
        Post post = postCRUDService.findById(id);
        User postOwner = post.getUser();
        User commentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        boolean isFriend = friendRequestService.isFriend(postOwner.getId(), commentUser.getId(), FRIEND_STATUS);
        if (isFriend || (postOwner.getId() == commentUser.getId())) {
            comment.setPost(post);
            comment.setUser(commentUser);
            return commentService.save(comment);
        } else {
            throw new CanNotCommentException();
        }
    }


    @PutMapping(value = "/posts/{id}/comments")
    public Comment getCommentById(@PathVariable int id, @RequestBody Comment comment) throws SQLIntegrityConstraintViolationException, EntityExistException, CanNotUpdateCommentException {
        User userComment = comment.getUser();
        Post post = postCRUDService.findById(id);
        User postOwner = post.getUser();
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (user.getId() == userComment.getId() || user.getId() == postOwner.getId()) {
            comment.setPost(post);
            commentService.save(comment);
            return comment;
        } else {
            throw new CanNotUpdateCommentException();
        }
    }

    @DeleteMapping(value = "/posts/{postId}/comments/{commentId}")
    public Comment deleteComments(@PathVariable("postId") int postId, @PathVariable("commentId") int commentId) throws CommentNotFound {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Comment comment = commentService.findById(commentId);
        User ownerComment = comment.getUser();
        User ownerPost = postCRUDService.findById(postId).getUser();
        if (comment == null) {
            throw new CommentNotFound();
        } else {
            if (user.getId() == ownerComment.getId() || user.getId() == ownerPost.getId()) {
                commentService.delete(commentId);
                return comment;
            } else {
                throw new CommentNotFound();
            }
        }
    }

    @GetMapping(value = "/notification/comments")
    public List<Comment> getNewAllCommentsByUserId() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Comment> commentList = commentService.findAllCommentByUserId(user.getId());
        List<Comment> comments;
        List<Comment> tenComment;
        if (commentList.size() < 10) {
            comments = commentList.subList(0, commentList.size());
            Collections.reverse(comments);
            return comments;
        } else {
            tenComment = commentList.subList(commentList.size() - 10, commentList.size());
            Collections.reverse(tenComment);
            return tenComment;
        }
    }

    @GetMapping(value = "/users/{id}/comments")
    public List<Comment> getAllCommentsByUserId(@PathVariable int id) {
        List<Comment> commentList = commentService.findAllCommentByUserId(id);
        return commentList;
    }

    @ExceptionHandler(CommentNotFound.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleCommentNotFoundException() {
        return "{\"error\":\"Comment not found!\"}";
    }

    @ExceptionHandler(CanNotUpdateCommentException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public String handleCommentNotUpdateException() {
        return "{\"error\":\"Comment can not update!\"}";
    }

    @ExceptionHandler(CanNotCommentException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public String handleExistRequestException() {
        return "{\"error\":\"Cannot comment because not friend\"}";
    }
}
