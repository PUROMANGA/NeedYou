package com.example.shopingplusassignment.domain.pay.repository;

import com.example.shopingplusassignment.domain.pay.entity.Pay;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PayRepository extends JpaRepository<Pay, Long> {
}
