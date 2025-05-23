package com.example.shopingplusassignment.domain.comment.dto;

import lombok.Getter;

import com.example.shopingplusassignment.domain.comment.entity.Comment;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({
	"id",
	"userId",
	"title",
	"description",
	"rating",
	"createdAt",
	"modifiedAt"
})
@Getter
public class CommentGetInfoDto {
	private Long Id;
	private Long userId;
	private String title;
	private String description;
	private int rating;
	private String createdAt;
	private String modifiedAt;
	private Long LikeCount;

	public CommentGetInfoDto(Comment comment) {
		this.Id = comment.getId();
		this.userId = comment.getUser().getId();
		this.title = comment.getTitle();
		this.description = comment.getDescription();
		this.rating = comment.getRating();
		this.createdAt = comment.getCreatTime().toString();
		this.modifiedAt =comment.getModifiedTime().toString();
	}
	public void updateLikeCount(Long likeCount){
		this.LikeCount =likeCount;
	}
}
