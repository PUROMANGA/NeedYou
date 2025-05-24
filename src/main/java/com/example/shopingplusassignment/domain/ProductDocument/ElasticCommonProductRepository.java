package com.example.shopingplusassignment.domain.ProductDocument;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ElasticCommonProductRepository extends ElasticsearchRepository<ProductDocument, Long>, CustomElasticCommonProductRepository {

    Slice<com.example.shopingplusassignment.domain.ProductDocument.ProductDocument> searchByKeyword(String keyword, Pageable pageable);

}
