package com.example.shopingplusassignment.domain.product.repository;

import com.example.shopingplusassignment.domain.product.dto.ProductStockDto;
import com.example.shopingplusassignment.domain.product.entity.QProduct;
import com.example.shopingplusassignment.domain.productOrder.entity.QProductOrder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.LockModeType;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor

public class CustomProductRepositoryImpl implements CustomProductRepository{

    private final JPAQueryFactory jpaQueryFactory;

    QProductOrder productOrder = QProductOrder.productOrder;
    QProduct product = QProduct.product;

    @Override
    public ProductStockDto findProductStockAndProductAmountByProductId(Long productId) {

        return jpaQueryFactory
                .select(Projections.constructor(ProductStockDto.class,
                        productOrder.amount,
                        product))
                .from(product)
                .join(productOrder).on(product.id.eq(productOrder.productId))
                .where(product.id.eq(productId))
                .setLockMode(LockModeType.PESSIMISTIC_WRITE)
                .fetchOne();
    }
}
