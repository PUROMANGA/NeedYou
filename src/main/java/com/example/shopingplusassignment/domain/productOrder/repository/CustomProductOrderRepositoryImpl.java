package com.example.shopingplusassignment.domain.productOrder.repository;


import com.example.shopingplusassignment.domain.order.dto.ResponseSavedOrderListDto;
import com.example.shopingplusassignment.domain.order.entity.QOrder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

@RequiredArgsConstructor

public class CustomProductOrderRepositoryImpl implements CustomProductOrderRepository {

    private final JPAQueryFactory jpaQueryFactory;

    QOrder order = QOrder.order;

    @Override
    public Page<ResponseSavedOrderListDto> findOrderPageByOrderId(Long orderId, Pageable pageable) {

        List<ResponseSavedOrderListDto> result = jpaQueryFactory
                .select(Projections.constructor(ResponseSavedOrderListDto.class,
                        order,
                        order.creatTime,
                        order.modifiedTime))
                .from(order)
                .where(order.id.eq(orderId))
                .fetch();

        long total = jpaQueryFactory
                .select(order.count())
                .from(order)
                .where(order.id.eq(orderId))
                .fetchOne();

        return new PageImpl<>(result, pageable, total);
    }
}
