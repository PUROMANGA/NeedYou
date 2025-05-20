package com.example.shopingplusassignment.domain.cart.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter

public class CartRequestDto {

    @NotNull
    private Long amount;
}
