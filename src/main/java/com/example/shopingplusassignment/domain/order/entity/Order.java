package com.example.shopingplusassignment.domain.order.entity;

import base_entity.BaseEntity;
import com.example.shopingplusassignment.domain.order.common.OrderStatus;
import com.example.shopingplusassignment.domain.productOrder.entity.ProductOrder;
import com.example.shopingplusassignment.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.List;
import java.util.ArrayList;

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

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus orderStatus;

    @OneToMany(mappedBy = "order", cascade = CascadeType.PERSIST)
    private List<ProductOrder> productOrderList = new ArrayList<>();

    public Order(User user, Long addressId) {
        this.user = user;
        this.addressId = addressId;
    }

    public void changeOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }
}
