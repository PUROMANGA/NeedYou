package com.example.shopingplusassignment.global.Event;

import com.example.shopingplusassignment.domain.product.entity.Product;
import org.springframework.context.ApplicationEvent;

public class ElasticsearchRegistrationEvent extends ApplicationEvent {

    private final Product product;

    public ElasticsearchRegistrationEvent(Object source, Product product) {
        super(source);
        this.product = product;
    }

    public Product getProduct() {
        return product;
    }
}
