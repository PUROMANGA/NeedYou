package com.example.shopingplusassignment.domain.comment.dto;

import lombok.Getter;

@Getter
public class CommentMessageResponseDto {
	String State;

	String Message;

	public CommentMessageResponseDto(String state, String message) {
		State = state;
		Message = message;
	}
}
