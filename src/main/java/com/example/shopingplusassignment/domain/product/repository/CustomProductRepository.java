package com.example.shopingplusassignment.domain.product.repository;

import com.example.shopingplusassignment.domain.product.dto.ProductStockDto;
import com.example.shopingplusassignment.domain.product.entity.Product;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.Lock;

public interface CustomProductRepository {

    ProductStockDto findProductStockAndProductAmountByProductId(Long productId);
}
