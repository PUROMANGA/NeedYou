package com.example.shopingplusassignment.domain.pay.dto;

import com.example.shopingplusassignment.domain.pay.entity.PaymentMethod;
import lombok.Getter;

@Getter

public class PayRequestDto {

    private String name;
    private PaymentMethod paymentMethod;
}
