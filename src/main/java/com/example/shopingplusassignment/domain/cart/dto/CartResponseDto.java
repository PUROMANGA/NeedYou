package com.example.shopingplusassignment.domain.cart.dto;

import com.example.shopingplusassignment.domain.cart.entity.Cart;
import com.example.shopingplusassignment.domain.product.entity.Product;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter

public class CartResponseDto {

    private Long cartId;
    private Long productId;
    private Long userId;
    private String name;
    private Long amount;
    private Long price;
    private LocalDateTime creatTime;
    private LocalDateTime modifiedTime;

    public CartResponseDto(Cart cart, Product product) {
        this.cartId = cart.getCartId();
        this.productId = cart.getProductId();
        this.userId = cart.getUserId();
        this.name = product.getName();
        this.amount = cart.getAmount();
        this.price = product.getPrice();
        this.creatTime = cart.getCreatTime();
        this.modifiedTime = cart.getModifiedTime();
    }
}
