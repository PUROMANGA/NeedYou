package com.example.shopingplusassignment.domain.comment.entity;

import base_entity.BaseEntity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import lombok.Getter;

import com.example.shopingplusassignment.domain.comment.dto.CommentRequestDto;
import com.example.shopingplusassignment.domain.order.entity.Order;
import com.example.shopingplusassignment.domain.product.entity.Product;
import com.example.shopingplusassignment.domain.user.entity.User;

@Getter
@Entity
@Table(name = "comments")
public class Comment extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	User user;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_id")
    Product product;

	@Column(nullable = false)
	String title;

	@Column(nullable = false)
	String description;

	@Column(nullable = false)
	int rating;

	boolean isDelete;
	LocalDateTime deletedAt;

	public Comment(){

	}

	public Comment(CommentRequestDto dto) {
		this.title = dto.getTitle();
		this.description = dto.getDescription();
		this.rating = dto.getRating();
	}
	public Comment(CommentRequestDto dto, User user, Product product){
		this.title = dto.getTitle();
		this.description = dto.getDescription();
		this.rating = dto.getRating();
		this.user = user;
		this.product = product;

	}
	//TestCode 전용
	public Comment(String title, String description, int rating, Order order){
		this.title = title;
		this.description = description;
		this.rating = rating;
		this.user = order.getUser();

	}

	public void markAsDeleted(boolean isDelete, LocalDateTime now){
		this.isDelete = isDelete;
		this.deletedAt = now;
	}

}
