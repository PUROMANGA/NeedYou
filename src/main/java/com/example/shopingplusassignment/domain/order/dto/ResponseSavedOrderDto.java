package com.example.shopingplusassignment.domain.order.dto;

import com.example.shopingplusassignment.domain.order.common.OrderStatus;
import com.example.shopingplusassignment.domain.order.entity.Order;
import com.example.shopingplusassignment.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor

public class ResponseSavedOrderDto {
    private Long id;
    private User user;
    private Long addressId;
    private OrderStatus orderStatus;

    public ResponseSavedOrderDto(Order order) {
        this.id = order.getId();
        this.user = order.getUser();
        this.addressId = order.getAddressId();
        this.orderStatus = order.getOrderStatus();
    }
}
