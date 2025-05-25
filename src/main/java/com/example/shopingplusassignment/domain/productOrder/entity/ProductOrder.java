package com.example.shopingplusassignment.domain.productOrder.entity;


import com.example.shopingplusassignment.domain.order.entity.Order;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class ProductOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Long price;

    @Column(nullable = false)
    private Long amount;

    @Column(nullable = false)
    private Long totalPrice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @Column(nullable = false)
    private Long productId;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime creatTime;

    @LastModifiedDate
    private LocalDateTime modifiedTime;

    public ProductOrder(String name, Long price, Long amount, Long totalPrice, Order order, Long productId) {
        this.name = name;
        this.price = price;
        this.amount = amount;
        this.totalPrice = totalPrice;
        this.order = order;
        this.productId = productId;
    }
}
