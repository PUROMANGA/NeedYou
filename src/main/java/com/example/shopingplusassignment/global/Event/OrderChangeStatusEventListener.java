package com.example.shopingplusassignment.global.Event;

import com.example.shopingplusassignment.domain.order.common.OrderStatus;
import com.example.shopingplusassignment.domain.order.entity.Order;
import com.example.shopingplusassignment.domain.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor

public class OrderChangeStatusEventListener {

    private final OrderRepository orderRepository;

    @Async
    @EventListener
    public void handleOrderChangeStatusEvent(OrderChangeStatusEvent orderChangeStatusEvent) {
        Long orderId = orderChangeStatusEvent.getOrderId();
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new RuntimeException("주문이 존재하지 않습니다."));
        order.changeOrderStatus(OrderStatus.PAID);
        orderRepository.save(order);
    }
}
