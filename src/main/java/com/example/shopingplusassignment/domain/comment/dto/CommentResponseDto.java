package com.example.shopingplusassignment.domain.comment.dto;

import java.time.LocalDateTime;

import lombok.Getter;

import com.example.shopingplusassignment.domain.comment.entity.Comment;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({
	"id",
	"productId",
	"userId",
	"title",
	"description",
	"rating",
	"likeCount",
	"createdAt",
	"modifiedAt"
})
@Getter
public class CommentResponseDto {
	private Long id;
	private Long productId;
	private Long userId;
	private String title;
	private String description;
	private int rating;
	private Long likeCount;
	private LocalDateTime createdAt;
	private LocalDateTime modifiedAt;

	public CommentResponseDto() {

	}
	public CommentResponseDto(Comment comment) {
		this.id = comment.getId();
		this.productId = comment.getProduct().getId();
		this.userId = comment.getUser().getId();
		this.title = comment.getTitle();
		this.description = comment.getDescription();
		this.rating = comment.getRating();
		this.likeCount = comment.getLikeCount();
		this. createdAt = comment.getCreatTime();
		this.modifiedAt = comment.getModifiedTime();
	}
}
