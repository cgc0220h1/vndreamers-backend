package com.codegym.vndreamers.repositories;

import com.codegym.vndreamers.models.PostReaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReactionRepository extends JpaRepository<PostReaction, Integer> {
}
