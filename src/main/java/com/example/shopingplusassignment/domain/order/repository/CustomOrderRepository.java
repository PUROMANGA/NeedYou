package com.example.shopingplusassignment.domain.order.repository;

import com.example.shopingplusassignment.domain.order.dto.ResponseOrderDto;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CustomOrderRepository {

    List<ResponseOrderDto> findResponseOrderDtoByUserId(Long userId);
}
