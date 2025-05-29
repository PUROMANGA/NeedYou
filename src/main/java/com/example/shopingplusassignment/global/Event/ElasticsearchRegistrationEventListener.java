package com.example.shopingplusassignment.global.Event;

import com.example.shopingplusassignment.domain.ProductDocument.ElasticCommonProductRepository;
import com.example.shopingplusassignment.domain.ProductDocument.ProductDocument;
import com.example.shopingplusassignment.domain.product.entity.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor

public class ElasticsearchRegistrationEventListener {

    private final ElasticCommonProductRepository elasticCommonProductRepository;

    @Async
    @EventListener
    public void handleElasticsearchRegistrationEventListener(ElasticsearchRegistrationEvent elasticsearchRegistrationEvent) {
        Product product = elasticsearchRegistrationEvent.getProduct();
        elasticCommonProductRepository.save(new ProductDocument(product));
    }
}
