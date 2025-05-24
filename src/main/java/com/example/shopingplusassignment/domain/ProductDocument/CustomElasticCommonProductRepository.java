package com.example.shopingplusassignment.domain.ProductDocument;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface CustomElasticCommonProductRepository {
    Slice<ProductDocument> searchByKeyword(String keyword, Pageable pageable);
}
