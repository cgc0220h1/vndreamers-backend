package com.codegym.vndreamers.apis;

import com.codegym.vndreamers.exceptions.PostNotFoundException;
import com.codegym.vndreamers.models.Post;
import com.codegym.vndreamers.models.PostReaction;
import com.codegym.vndreamers.models.User;
import com.codegym.vndreamers.services.friendrequest.FriendRequestService;
import com.codegym.vndreamers.services.post.PostCRUDService;
import com.codegym.vndreamers.services.reaction.ReactionService;
import com.codegym.vndreamers.services.user.UserCRUDService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin("*")
public class UserAPI {

    public static final int PUBLIC_POST = 1;
    public static final int PROTECT_POST = 2;
    public static final int PRIVATE_POST = 3;
    public static final int FRIEND_STATUS = 1;

    private final UserCRUDService userCRUDService;

    private final PostCRUDService postCRUDService;

    private final ReactionService reactionService;

    private FriendRequestService friendRequestService;

    @Autowired
    public UserAPI(UserCRUDService userCRUDService, PostCRUDService postCRUDService, ReactionService reactionService, FriendRequestService friendRequestService) {
        this.userCRUDService = userCRUDService;
        this.postCRUDService = postCRUDService;
        this.reactionService = reactionService;
        this.friendRequestService = friendRequestService;
    }

    @GetMapping("/users/{username}")
    public User getUserByUsername(@PathVariable("username") String username) {
        return userCRUDService.findByUsername(username);
    }

    @PutMapping("/users")
    public User updateProfileUser(@RequestBody User user) {
        User userToken = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (userToken.getId() == user.getId()) {
            User myUser = userCRUDService.findById(userToken.getId());
            myUser.setFirstName(user.getFirstName());
            myUser.setLastName(user.getLastName());
            myUser.setBirthDate(user.getBirthDate());
            myUser.setConfirmPassword(myUser.getPassword());
            myUser.setUsername(user.getUsername());
            myUser.setAddress(user.getAddress());
            myUser.setPhoneNumber(user.getPhoneNumber());
            myUser.setAboutMe(user.getAboutMe());
            return userCRUDService.updateProfileUser(myUser);
        } else {
            return null;
        }
    }

    @GetMapping("/users/{id}/posts")
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
}
