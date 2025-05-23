package com.example.shopingplusassignment.domain.comment.dto;

import lombok.Getter;

import com.example.shopingplusassignment.domain.comment.entity.Comment;

@Getter
public class CommentResponseDto {
	private Long Id;
	private String title;
	private String description;
	private int rating;
	private String createdAt;
	private String modifiedAt;

	public CommentResponseDto(Comment comment) {
		this.Id = comment.getId();
		this.title = comment.getTitle();
		this.description = comment.getDescription();
		this.rating = comment.getRating();
		this. createdAt = comment.getCreatTime().toString();
		this.modifiedAt = comment.getModifiedTime().toString();
	}
}
