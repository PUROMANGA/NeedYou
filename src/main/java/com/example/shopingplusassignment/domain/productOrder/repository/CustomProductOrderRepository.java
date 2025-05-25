package com.example.shopingplusassignment.domain.productOrder.repository;

import com.example.shopingplusassignment.domain.order.dto.ResponseSavedOrderDto;
import com.example.shopingplusassignment.domain.order.dto.ResponseSavedOrderListDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CustomProductOrderRepository {
      Page<ResponseSavedOrderListDto> findOrderPageByOrderId(Long orderId, Pageable pageable);
}
