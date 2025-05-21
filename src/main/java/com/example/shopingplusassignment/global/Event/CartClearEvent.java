package com.example.shopingplusassignment.global.Event;

import org.springframework.context.ApplicationEvent;

public class CartClearEvent extends ApplicationEvent {

    private final Long userId;

    public CartClearEvent(Object source, Long userId) {
        super(source);
        this.userId = userId;
    }

    public Long getUserId() {
        return userId;
    }
}
