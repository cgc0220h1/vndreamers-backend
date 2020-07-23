package com.codegym.vndreamers.repositories;

import com.codegym.vndreamers.models.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface PostRepository extends JpaRepository<Post, Integer> {
}
