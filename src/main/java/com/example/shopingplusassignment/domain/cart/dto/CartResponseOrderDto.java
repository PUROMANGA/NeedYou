package com.example.shopingplusassignment.domain.cart.dto;

import com.example.shopingplusassignment.domain.order.entity.Order;
import com.example.shopingplusassignment.domain.productOrder.entity.ProductOrder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter

public class CartResponseOrderDto {

    private Long productId;
    private String name;
    private Long price;
    private Long amount;
    private Long totalPrice;
    private Order order;
    private LocalDateTime creatTime;
    private LocalDateTime modifiedTime;

    public CartResponseOrderDto(Long productId, String name, Long price, Long amount, Long totalPrice, Order order, LocalDateTime creatTime, LocalDateTime modifiedTime) {
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.amount = amount;
        this.totalPrice = totalPrice;
        this.order = order;
        this.creatTime = creatTime;
        this.modifiedTime = modifiedTime;
    }

    public ProductOrder toEntity() {
        return ProductOrder.builder()
                .name(this.name)
                .price(this.price)
                .amount(this.amount)
                .totalPrice(this.totalPrice)
                .order(this.order)
                .productId(this.productId)
                .creatTime(this.creatTime)
                .modifiedTime(this.modifiedTime)
                .build();
    }
}
