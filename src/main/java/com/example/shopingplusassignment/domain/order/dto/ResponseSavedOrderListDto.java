package com.example.shopingplusassignment.domain.order.dto;

import com.example.shopingplusassignment.domain.order.entity.Order;
import com.example.shopingplusassignment.domain.productOrder.entity.ProductOrder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor

public class ResponseSavedOrderListDto {
    private Order order;
    private LocalDateTime creatTime;
    private LocalDateTime modifiedTime;

    public ResponseSavedOrderListDto(Order order, LocalDateTime creatTime, LocalDateTime modifiedTime) {
        this.order = order;
        this.creatTime = creatTime;
        this.modifiedTime = modifiedTime;
    }
}
