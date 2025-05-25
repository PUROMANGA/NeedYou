package com.example.shopingplusassignment.domain.comment.entity;


import java.time.LocalDateTime;

import com.example.shopingplusassignment.base_entity.BaseEntity;
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
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_id")
	private Product product;

	@Column(nullable = false)
	private String title;

	@Column(nullable = false)
	private String description;

	@Column(nullable = false)
	private int rating;

	@Column(name= "like_Count")
	private Long likeCount = 0L;

	@Column(name ="is_deleted")
	private boolean isDelete;

	private LocalDateTime deletedAt;


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
    public void increaseLikeCount(boolean status){
		if (status) {
			this.likeCount++;
		} else {
			this.likeCount = Math.max(0, this.likeCount - 1);
		}
	}
	public void updateComment(String title, String description, int rating){
		this.title = title;
		this.description = description;
		this.rating = rating;
	}
}
