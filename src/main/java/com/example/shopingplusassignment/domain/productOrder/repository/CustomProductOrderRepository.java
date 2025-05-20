package com.example.shopingplusassignment.domain.productOrder.repository;

import com.example.shopingplusassignment.domain.order.dto.ResponseOrderDto;
import com.example.shopingplusassignment.domain.order.dto.ResponseSavedOrderListDto;
import com.example.shopingplusassignment.domain.productOrder.dto.ResponseProductOrderDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CustomProductOrderRepository {
    List<ResponseProductOrderDto> findResponseProductOrderDtoByOrderId(Long orderId);
    Page<ResponseSavedOrderListDto> findOrderPageByOrderId(Long orderId, Pageable pageable);
}
