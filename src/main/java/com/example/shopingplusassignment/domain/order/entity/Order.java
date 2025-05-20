package com.example.shopingplusassignment.domain.order.entity;

import base_entity.BaseEntity;
import com.example.shopingplusassignment.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "orders")
@AllArgsConstructor
@NoArgsConstructor

public class Order extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "users_id")
    private User user;

    private Long addressId;

    public Order(User user, Long addressId) {
        this.user = user;
        this.addressId = addressId;
    }
}
