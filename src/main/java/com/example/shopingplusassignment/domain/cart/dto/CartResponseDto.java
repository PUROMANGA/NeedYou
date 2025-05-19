package com.example.shopingplusassignment.domain.cart.dto;

import com.example.shopingplusassignment.domain.cart.entity.Cart;
import com.example.shopingplusassignment.domain.product.entity.Product;
import lombok.Getter;

@Getter

public class CartResponseDto {

    private Long cartId;
    private Long productId;
    private Long userId;
    private String name;
    private Long amount;
    private Long price;

    public CartResponseDto(Cart cart, Product product) {
        this.cartId = cart.getCartId();
        this.productId = cart.getProductId();
        this.userId = cart.getUserId();
        this.name = product.getName();
        this.amount = cart.getAmount();
        this.price = product.getPrice();
    }

    public CartResponseDto(Cart cart) {
        this.cartId = cart.getCartId();
        this.productId = cart.getProductId();
        this.userId = cart.getUserId();
        this.name = product.getName();
        this.amount = cart.getAmount();
        this.price = product.getPrice();
    }

}
