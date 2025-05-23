package com.example.shopingplusassignment.domain.product.repository;

import com.example.shopingplusassignment.domain.product.common.ProductCategory;
import com.example.shopingplusassignment.domain.product.dto.ResponseProductDto;
import com.example.shopingplusassignment.domain.product.entity.Product;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("select p " +
            "from Product p " +
            "Where p.name LIKE CONCAT('%', :keyword, '%') OR p.description LIKE CONCAT('%', :keyword, '%')")
    Slice<Product> findByKeyword(String keyword, Pageable pageable);

    @Query("select p " +
            "from Product p " +
            "Where p.productCategory = :productCategory")
    Slice<Product> findProductByProductCategory(ProductCategory productCategory, Pageable pageable);

}
