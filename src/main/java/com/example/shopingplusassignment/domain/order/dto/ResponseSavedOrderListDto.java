package com.example.shopingplusassignment.domain.order.dto;

import com.example.shopingplusassignment.domain.productOrder.entity.ProductOrder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor

public class ResponseSavedOrderListDto {
    private ProductOrder productOrder;
    private LocalDateTime creatTime;
    private LocalDateTime modifiedTime;

    public ResponseSavedOrderListDto(ProductOrder productOrder, LocalDateTime creatTime, LocalDateTime modifiedTime) {
        this.productOrder = productOrder;
        this.creatTime = creatTime;
        this.modifiedTime = modifiedTime;
    }
}
