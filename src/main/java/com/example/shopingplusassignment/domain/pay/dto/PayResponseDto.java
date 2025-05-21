package com.example.shopingplusassignment.domain.pay.dto;

import com.example.shopingplusassignment.domain.pay.entity.Pay;
import com.example.shopingplusassignment.domain.pay.entity.PaymentMethod;
import lombok.Getter;

@Getter

public class PayResponseDto {

    private Long id;
    private String name;
    private PaymentMethod paymentMethod;

    public PayResponseDto(Pay pay) {
        this.id = pay.getId();
        this.name = pay.getName();
        this.paymentMethod = pay.getPaymentMethod();
    }
}
