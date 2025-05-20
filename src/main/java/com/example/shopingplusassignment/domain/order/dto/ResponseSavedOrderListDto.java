package com.example.shopingplusassignment.domain.order.dto;

import com.example.shopingplusassignment.domain.order.entity.Order;
import com.example.shopingplusassignment.domain.productOrder.entity.ProductOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor

public class ResponseSavedOrderListDto {
    private Order order;
    private ProductOrder productOrder;
}
