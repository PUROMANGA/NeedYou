package com.example.shopingplusassignment.domain.comment.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.shopingplusassignment.domain.comment.entity.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {

	Optional<Comment> findById(Long id);

}
