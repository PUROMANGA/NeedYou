package com.example.shopingplusassignment.domain.order.repository;

import com.example.shopingplusassignment.domain.order.dto.ResponseOrderDto;
import java.util.List;

public interface CustomOrderRepository {

    List<ResponseOrderDto> findOrdersByEmail(Long userId);
}
