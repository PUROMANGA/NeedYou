package com.example.shopingplusassignment.domain.comment.entity;

import base_entity.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.Getter;

import com.example.shopingplusassignment.domain.comment.dto.CommentRequestDto;

@Getter
@Entity
@Table(name = "comments")
public class Comment extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;

	@Column(nullable = false)
	String title;

	@Column(nullable = false)
	String description;

	@Column(nullable = false)
	int rating;

	public Comment(){

	}

	public Comment(CommentRequestDto dto) {
		this.title = dto.getTitle();
		this.description = dto.getDescription();
		this.rating = dto.getRating();
	}

}
