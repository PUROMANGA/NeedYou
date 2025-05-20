package com.example.shopingplusassignment.domain.order.repository;

import com.example.shopingplusassignment.domain.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long>, CustomOrderRepository {
}
