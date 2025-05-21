package com.example.shopingplusassignment.domain.comment.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CommentMessageResponseDto {
	private String state;
	private String message;

	public CommentMessageResponseDto(String state, String message) {
		this.state = state;
		this.message = message;
	}

}
