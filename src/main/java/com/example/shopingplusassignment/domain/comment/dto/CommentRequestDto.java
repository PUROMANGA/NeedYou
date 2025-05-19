package com.example.shopingplusassignment.domain.comment.dto;

import jakarta.persistence.Column;

import lombok.Getter;

@Getter
public class CommentRequestDto {

	String title;

	String description;

	int rating;
}
