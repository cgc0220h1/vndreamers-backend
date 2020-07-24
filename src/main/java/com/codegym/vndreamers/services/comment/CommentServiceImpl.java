package com.codegym.vndreamers.services.comment;

import com.codegym.vndreamers.exceptions.EntityExistException;
import com.codegym.vndreamers.models.Comment;
import com.codegym.vndreamers.models.Post;
import com.codegym.vndreamers.repositories.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import java.util.Optional;

@Service
@PropertySource("classpath:config/status.properties")
public class CommentServiceImpl implements CommentService {
    @Value("${entity.exist}")
    private int statusExist;

    @Value("${entity.deleted}")
    private int statusDelete;

    private final CommentRepository commentRepository;

    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @Override
    public List<Comment> findAllExistByPost(Post post) {
        return commentRepository.findAllByPostAndStatus(post, statusExist);
    }

    @Override
    public Page<Comment> findAllExistByPost(Post post, Pageable pageable) {
        return commentRepository.findAllByPostAndStatus(post, statusExist, pageable);
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
        return null;
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
        Optional <Comment>optionalComment = commentRepository.findById(id);
        if (optionalComment.isPresent()) {
            Comment comment = optionalComment.get();
            commentRepository.save(comment);
            return true;
        }
        return false;
    }
}
