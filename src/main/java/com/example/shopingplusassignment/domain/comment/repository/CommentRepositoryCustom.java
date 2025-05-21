package com.example.shopingplusassignment.domain.comment.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.example.shopingplusassignment.domain.comment.dto.CommentGetInfoDto;
import com.example.shopingplusassignment.domain.comment.entity.Comment;

public interface CommentRepositoryCustom {

	Page<CommentGetInfoDto> findCommentsByDynamicCondition(int min, int max, Pageable pageable);
}
