package com.example.shopingplusassignment.domain.order.dto;

import com.example.shopingplusassignment.domain.address.entity.Address;
import com.example.shopingplusassignment.domain.cart.entity.Cart;
import lombok.Getter;

import java.util.List;

@Getter

public class ResponseOrderDto {
    private String productName;
    private Long price;
    private Address address;
    private Cart carts;
    private Long totalPrice;

    public ResponseOrderDto(String productName, Long price, Address address, Cart carts, Long totalPrice) {
        this.productName = productName;
        this.price = price;
        this.address = address;
        this.carts = carts;
        this.totalPrice = totalPrice;
    }
    //    private PayStatus payStatus;
}
