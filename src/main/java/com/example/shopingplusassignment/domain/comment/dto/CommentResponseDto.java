package com.example.shopingplusassignment.domain.comment.dto;

import lombok.Getter;

import com.example.shopingplusassignment.domain.comment.entity.Comment;

@Getter
public class CommentResponseDto {
	Long Id;

	String title;

	String description;

	int rating;

	public CommentResponseDto(Comment comment) {
		this.Id = comment.getId();
		this.title = comment.getTitle();
		this.description = comment.getDescription();
		this.rating = comment.getRating();
	}
}
