package com.codegym.vndreamers.services.comment;

import com.codegym.vndreamers.exceptions.EntityExistException;
import com.codegym.vndreamers.models.Comment;
import com.codegym.vndreamers.models.Post;
import com.codegym.vndreamers.repositories.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

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
        return null;
    }

    @Override
    public Comment update(Comment model) {
        return null;
    }

    @Override
    public boolean delete(int id) {
        return false;
    }
}
