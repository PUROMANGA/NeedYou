package com.example.shopingplusassignment.global.Event;

import org.springframework.context.ApplicationEvent;

public class OrderChangeStatusEvent extends ApplicationEvent {

    private final Long orderId;

    public OrderChangeStatusEvent(Object source, Long orderId) {
        super(source);
        this.orderId = orderId;
    }
    public Long getOrderId() {
        return orderId;
    }
}
