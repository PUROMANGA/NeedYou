package com.example.shopingplusassignment.domain.comment.dto;

import lombok.Getter;

@Getter
public class CommentLikeResponseDto {
	private String state;
	private String message;
	private Long likeCount;
	public CommentLikeResponseDto(String state, String message, Long likeCount) {
		this.state = state;
		this.message = message;
		this.likeCount = likeCount;
	}
}
