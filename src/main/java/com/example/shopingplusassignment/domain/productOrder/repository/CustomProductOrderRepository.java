package com.example.shopingplusassignment.domain.productOrder.repository;

import com.example.shopingplusassignment.domain.order.dto.ResponseSavedOrderListDto;
import com.example.shopingplusassignment.domain.productOrder.dto.ResponseProductOrderDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CustomProductOrderRepository {
    List<ResponseProductOrderDto> responseProductOrderDto(Long orderId);
    Page<ResponseSavedOrderListDto> findByOrderId(Long orderId, Pageable pageable);
}
