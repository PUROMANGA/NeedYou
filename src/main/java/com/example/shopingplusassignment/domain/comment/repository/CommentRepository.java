package com.example.shopingplusassignment.domain.comment.repository;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.shopingplusassignment.domain.comment.entity.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long>, CommentRepositoryCustom {

	@EntityGraph(attributePaths = {"user"})
	Optional<Comment> findByIdAndDeletedAtIsNull(Long id);

	long countByProductIdAndDeletedAtIsNull(Long product_id);

	// Page<Comment> findAllByRatingBetweenAndDeletedAtIsNull(int min, int max, Pageable pageable);

}
