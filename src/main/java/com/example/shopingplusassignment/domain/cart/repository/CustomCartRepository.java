package com.example.shopingplusassignment.domain.cart.repository;

import com.example.shopingplusassignment.domain.cart.dto.CartResponseOrderDto;
import java.util.List;

public interface CustomCartRepository {

    List<CartResponseOrderDto> findResponseCartOrderDtoByOrderId(Long orderId);
}
