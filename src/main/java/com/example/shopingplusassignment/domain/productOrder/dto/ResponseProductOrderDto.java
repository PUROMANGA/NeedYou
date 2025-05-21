package com.example.shopingplusassignment.domain.productOrder.dto;

import com.example.shopingplusassignment.domain.order.entity.Order;
import com.example.shopingplusassignment.domain.product.entity.Product;
import com.example.shopingplusassignment.domain.productOrder.entity.ProductOrder;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter

public class ResponseProductOrderDto {

    private Long id;
    private String name;
    private Long price;
    private Long amount;
    private Long totalPrice;
    private Order order;
    private LocalDateTime creatTime;
    private LocalDateTime modifiedTime;

    public ResponseProductOrderDto(Long id, String name, Long price, Long amount, Long totalPrice, Order order, LocalDateTime creatTime, LocalDateTime modifiedTime) {
        this.id = id;
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
                .id(this.id)
                .name(this.name)
                .price(this.price)
                .amount(this.amount)
                .totalPrice(this.totalPrice)
                .order(this.order)
                .creatTime(this.creatTime)
                .modifiedTime(this.modifiedTime)
                .build();
    }
}
