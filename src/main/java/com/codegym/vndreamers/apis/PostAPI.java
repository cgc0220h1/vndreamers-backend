package com.codegym.vndreamers.apis;

import com.codegym.vndreamers.exceptions.UserExistException;
import com.codegym.vndreamers.models.Post;
import com.codegym.vndreamers.models.User;
import com.codegym.vndreamers.services.auth.jwt.JWTIssuer;
import com.codegym.vndreamers.services.post.PostCRUDService;
import com.codegym.vndreamers.services.user.UserCRUDService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin("*")
public class PostAPI {

    @Autowired
    private JWTIssuer tokenProvider;

    @Autowired
    private UserCRUDService userCRUDService;

    @Autowired
    private PostCRUDService postCRUDService;

    @PostMapping("/posts")
    public ResponseEntity<String> savePosts(@RequestBody Post post, HttpServletRequest request) throws SQLIntegrityConstraintViolationException, UserExistException {
        String jwt = request.getHeader("Authorization");
        if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {
            String username = tokenProvider.getUsernameFromJWT(jwt);
            User user = userCRUDService.findByUsername(username);
            post.setUser(user);
            postCRUDService.save(post);
            return new ResponseEntity<>("ok", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Token Invalid", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/posts")
    public ResponseEntity<List<Post>> getAllPostsUser(HttpServletRequest request) {
        String jwt = request.getHeader("Authorization");
        System.out.println(jwt);
        if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {
            String username = tokenProvider.getUsernameFromJWT(jwt);
            User user = userCRUDService.findByUsername(username);
            List<Post> posts = postCRUDService.getAllByUserId(Integer.valueOf(user.getId()));
            return new ResponseEntity<>(posts, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }
}
