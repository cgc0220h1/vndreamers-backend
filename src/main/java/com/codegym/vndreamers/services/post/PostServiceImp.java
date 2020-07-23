package com.codegym.vndreamers.services.post;

import com.codegym.vndreamers.exceptions.UserExistException;
import com.codegym.vndreamers.models.Post;
import com.codegym.vndreamers.repositories.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

@Service
public class PostServiceImp implements PostCRUDService {

    @Autowired
    private PostRepository postRepository;

    @Override
    public List<Post> findAll() {
        return postRepository.findAll();
    }

    @Override
    public List<Post> findAll(Sort sort) {
        return postRepository.findAll(sort);
    }

    @Override
    public Page<Post> findAll(Pageable pageable) {
        return postRepository.findAll(pageable);
    }

    @Override
    public Post findById(int id) {
        return postRepository.findById(id).get();
    }

    @Override
    public Post save(Post model) throws SQLIntegrityConstraintViolationException, UserExistException {
        return postRepository.save(model);
    }

    @Override
    public Post update(Post model) {
        return null;
    }

    @Override
    public boolean delete(int id) {
        return false;
    }
}
