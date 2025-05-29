package com.example.shopingplusassignment.domain.comment.dto;

import lombok.Getter;

@Getter
public class CommentGetCountResponseDto {
	private Long productId;
	private Long count;

	public CommentGetCountResponseDto(Long productId, Long count) {
		this.productId = productId;
		this.count = count;
	}
}
