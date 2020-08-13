package com.codegym.vndreamers.services.comment;

import com.codegym.vndreamers.exceptions.EntityExistException;
import com.codegym.vndreamers.models.Comment;
import com.codegym.vndreamers.models.Post;
import com.codegym.vndreamers.repositories.CommentRepository;
import com.codegym.vndreamers.services.user.UserCRUDService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import java.util.Optional;

@Service
//@PropertySource("classpath:config/status.properties")
public class CommentServiceImpl implements CommentService {


    private final CommentRepository commentRepository;

    private final UserCRUDService userCRUDService;

    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository, UserCRUDService userCRUDService) {
        this.commentRepository = commentRepository;
        this.userCRUDService = userCRUDService;
    }

    @Override
    public Iterable<Comment> findAllByPost(Post post) {
        return commentRepository.findAllByPost(post);
    }

    @Override
    public List<Comment> findAllByPostId(Integer postId) {
        return commentRepository.findAllByPostId(postId);
    }

    @Override
    public List<Comment> findAll() {
        return null;
    }

    @Override
    public List<Comment> findAll(Sort sort) {
        return null;
    }

    @Override
    public Page<Comment> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public Comment findById(int id) {
        return commentRepository.findById(id).get();
    }

    @Override
    public List<Comment> findAllCommentByUserId(Integer id) {
        return commentRepository.findCommentByUserId(id);
    }

    @Override
    public Comment save(Comment model) throws SQLIntegrityConstraintViolationException, EntityExistException {
        return commentRepository.save(model);
    }

    @Override
    public Comment update(Comment model) {
        return null;
    }

    @Override
    public boolean delete(int id) {
        Comment comment = commentRepository.findById(id).get();
        if (comment != null){
            commentRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public void removeComment(Integer id) {
        commentRepository.deleteById(id);
    }
}
