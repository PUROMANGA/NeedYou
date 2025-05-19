package com.example.shopingplusassignment.domain.comment.entity;

import base_entity.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import lombok.Getter;

import com.example.shopingplusassignment.domain.comment.dto.CommentRequestDto;
import com.example.shopingplusassignment.domain.product.entity.Product;
import com.example.shopingplusassignment.domain.user.entity.User;

@Getter
@Entity
@Table(name = "comments")
public class Comment extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;

	@ManyToOne
	@JoinColumn(name = "user_id")
	User user;

	@ManyToOne
	@JoinColumn(name = "product_id")
    Product product;

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
	public Comment(CommentRequestDto dto,Order order){
		this.title = dto.getTitle();
		this.description = dto.getDescription();
		this.rating = dto.getRating();
		this.user = order.getUser();
		this.product = order.getProduct();
	}

}
