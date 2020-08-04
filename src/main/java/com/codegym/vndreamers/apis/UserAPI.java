package com.codegym.vndreamers.apis;

import com.codegym.vndreamers.exceptions.PostNotFoundException;
import com.codegym.vndreamers.models.Post;
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


    private FriendRequestService friendRequestService;

    @Autowired
    public UserAPI(UserCRUDService userCRUDService, PostCRUDService postCRUDService, FriendRequestService friendRequestService) {
        this.userCRUDService = userCRUDService;
        this.postCRUDService = postCRUDService;
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

        if (user.getId() == postOwner.getId()) {

            List<Post> allPosts = postCRUDService.getAllByUSerIdAndRelationShip(postOwner.getId(), PRIVATE_POST, postOwner.getId(), PROTECT_POST, postOwner.getId(), PUBLIC_POST);
            return allPosts;
//            if (allPosts != null) {
//                for (Post post : allPosts) {
//                    List<PostReaction> postReaction = reactionService.getAllReactionByPostId(post.getId());
//                    int likes = postReaction.size();
//                    post.setLikeQuantity(likes);
//                }
//                Collections.reverse(allPosts);
//                return allPosts;
//            } else {
//                throw new PostNotFoundException();
//            }
        }
        boolean isFriend = friendRequestService.isFriend(postOwner.getId(), user.getId(), FRIEND_STATUS);
        if (isFriend) {
            List<Post> friendPosts = postCRUDService.getAllByUSerIdAndRelationShip(postOwner.getId(), PUBLIC_POST, postOwner.getId(), PROTECT_POST, postOwner.getId(), PUBLIC_POST);
            return friendPosts;
//            if (friendPosts != null) {
//                for (Post post : friendPosts) {
//                    List<PostReaction> postReaction = reactionService.getAllReactionByPostId(post.getId());
//                    int likes = postReaction.size();
//                    post.setLikeQuantity(likes);
//                }
//                Collections.reverse(friendPosts);
//                return friendPosts;
//            } else {
//                throw new PostNotFoundException();
//            }
        } else {
            List<Post> publicPosts = postCRUDService.getAllByUSerIdAndRelationShip(postOwner.getId(), PUBLIC_POST, postOwner.getId(), PUBLIC_POST, postOwner.getId(), PUBLIC_POST);
            return publicPosts;
//            if (publicPosts != null) {
//                for (Post post : publicPosts) {
//                    List<PostReaction> postReaction = reactionService.getAllReactionByPostId(post.getId());
//                    int likes = postReaction.size();
//                    post.setLikeQuantity(likes);
//                }
//                Collections.reverse(publicPosts);
//                return publicPosts;
//            } else {
//                throw new PostNotFoundException();
//            }
        }
    }
}
