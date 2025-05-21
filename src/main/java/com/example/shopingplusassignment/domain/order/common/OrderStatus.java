package com.example.shopingplusassignment.domain.order.common;

import lombok.Getter;

@Getter

public enum OrderStatus {
    PENDING("미결제"),
    PAID("결제완료"),
    PROCESSING("주문진행중"),
    SHIPPED("배송중"),
    DELIVERED("배송완료");

    private String status;

    OrderStatus(String status) {
    }
}
