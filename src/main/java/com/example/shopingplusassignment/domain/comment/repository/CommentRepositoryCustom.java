package com.example.shopingplusassignment.domain.comment.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.example.shopingplusassignment.domain.comment.dto.CommentResponseDto;

public interface CommentRepositoryCustom {

	Page<CommentResponseDto> findCommentsByDynamicCondition(Long productId, Long min, Long max, Pageable pageable);
}
