package com.example.shopingplusassignment.domain.comment.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.example.shopingplusassignment.domain.comment.dto.CommentGetInfoDto;

public interface CommentRepositoryCustom {

	Page<CommentGetInfoDto> findCommentsByDynamicCondition(Long productId, int min, int max, Pageable pageable);
}
