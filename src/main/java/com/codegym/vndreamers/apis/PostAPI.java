package com.codegym.vndreamers.apis;

import com.codegym.vndreamers.exceptions.CanNotCommentException;
import com.codegym.vndreamers.exceptions.EntityExistException;
import com.codegym.vndreamers.exceptions.PostDeleteException;
import com.codegym.vndreamers.exceptions.PostNotFoundException;
import com.codegym.vndreamers.models.Comment;
import com.codegym.vndreamers.models.Post;
import com.codegym.vndreamers.models.PostReaction;
import com.codegym.vndreamers.models.User;
import com.codegym.vndreamers.services.comment.CommentService;
import com.codegym.vndreamers.services.friendrequest.FriendRequestService;
import com.codegym.vndreamers.services.post.PostCRUDService;
import com.codegym.vndreamers.services.reaction.ReactionService;
import com.codegym.vndreamers.services.user.UserCRUDService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin("*")
public class PostAPI {

    public static final int PUBLIC_POST = 1;
    public static final int PROTECT_POST = 2;
    public static final int PRIVATE_POST = 3;
    public static final int FRIEND_STATUS = 1;


    @Autowired
    private CommentService commentService;

    @Autowired
    private PostCRUDService postCRUDService;

    @Autowired
    private ReactionService reactionService;

    @Autowired
    private FriendRequestService friendRequestService;

    @Autowired
    private UserCRUDService userCRUDService;


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
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
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

    @GetMapping("/posts/{id}")
    public List<Post> getAllPostsOtherUser(@PathVariable int id) throws PostNotFoundException {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User postOwner = userCRUDService.findById(id);
        List<Post> posts;
        if (user.getId() == postOwner.getId()) {
            posts = postCRUDService.getAllByUSerIdAndRelationShip(postOwner.getId(), PRIVATE_POST, postOwner.getId(), PROTECT_POST, postOwner.getId(), PUBLIC_POST);
            if (posts != null) {
                for (Post post : posts) {
                    List<PostReaction> postReaction = reactionService.getAllReactionByPostId(post.getId());
                    int likes = postReaction.size();
                    post.setLikeQuantity(likes);
                }
                Collections.reverse(posts);
                return posts;
            } else {
                throw new PostNotFoundException();
            }
        }
        boolean isFriend = friendRequestService.isFriend(postOwner.getId(), user.getId(), FRIEND_STATUS);
        if (isFriend) {
            posts = postCRUDService.getAllByUSerIdAndRelationShip(postOwner.getId(), PUBLIC_POST, postOwner.getId(), PROTECT_POST, postOwner.getId(), PUBLIC_POST);
            if (posts != null) {
                for (Post post : posts) {
                    List<PostReaction> postReaction = reactionService.getAllReactionByPostId(post.getId());
                    int likes = postReaction.size();
                    post.setLikeQuantity(likes);
                }
                Collections.reverse(posts);
                return posts;
            } else {
                throw new PostNotFoundException();
            }
        } else {
            posts = postCRUDService.getAllByUSerIdAndRelationShip(postOwner.getId(), PUBLIC_POST, postOwner.getId(), PUBLIC_POST, postOwner.getId(), PUBLIC_POST);
            if (posts != null) {
                for (Post post : posts) {
                    List<PostReaction> postReaction = reactionService.getAllReactionByPostId(post.getId());
                    int likes = postReaction.size();
                    post.setLikeQuantity(likes);
                }
                Collections.reverse(posts);
                return posts;
            } else {
                throw new PostNotFoundException();
            }
        }
    }


    @DeleteMapping("/posts/{id}")
    public Post deletePostsUser(@PathVariable("id") int id) throws PostDeleteException {
        try {
            Post post = postCRUDService.findById(id);
            if (post.getStatus() == 0) {
                throw new PostDeleteException();
            }
        } catch (Exception e) {
            throw new PostDeleteException();
        }
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Post post = postCRUDService.deletePostByIdAndUserId(Integer.valueOf(id), Integer.valueOf(user.getId()));
        if (post != null) {
            return post;
        } else {
            return null;
        }
    }

//    @PutMapping("/posts/{id}")
//    public Object updatePost(@PathVariable("id") int id, @RequestBody Post post) throws SQLIntegrityConstraintViolationException, EntityExistException {
//        Post currentPost = postCRUDService.findById(id);
//        if (currentPost == null) {
//            return new ResponseEntity<Post>(HttpStatus.NOT_FOUND);
//        }
//        currentPost.setContent(post.getContent());
//        currentPost.setImage(post.getImage());
//        currentPost.setId(post.getId());
//        postCRUDService.save(currentPost);
//        return new ResponseEntity<>(currentPost, HttpStatus.OK);
//    }

    @PutMapping("/posts/{id}")
    public Post updatePost(@PathVariable("id") int id, @RequestBody Post post) throws SQLIntegrityConstraintViolationException, EntityExistException, PostNotFoundException {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Post currentPost = postCRUDService.findById(id);
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
