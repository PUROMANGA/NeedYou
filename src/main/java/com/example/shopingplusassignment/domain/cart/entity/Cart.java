package com.example.shopingplusassignment.domain.cart.entity;

import base_entity.BaseEntity;
import com.example.shopingplusassignment.domain.cart.dto.CartRequestDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "carts")

public class Cart extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cartId;

    @Column(nullable = false)
    private Long productId;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Long amount;

    public void update(CartRequestDto cartRequestDto) {
        this.amount = cartRequestDto.getAmount();
    }

    public Cart(Long productId, Long userId, CartRequestDto cartRequestDto) {
        this.productId = productId;
        this.userId = userId;
        this.amount = cartRequestDto.getAmount();
    }
}
