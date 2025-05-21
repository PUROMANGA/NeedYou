package com.example.shopingplusassignment.domain.cart.dto;

import com.example.shopingplusassignment.domain.address.entity.Address;
import lombok.Getter;

@Getter

public class CartProductDto {
    private Address address;
    private Long cartId;
    private String productName;
    private Long price;
    private Long totalPrice;

    public CartProductDto(Address address, Long cartId, String productName, Long price, Long totalPrice) {
        this.address = address;
        this.cartId = cartId;
        this.productName = productName;
        this.price = price;
        this.totalPrice = totalPrice;
    }
}
