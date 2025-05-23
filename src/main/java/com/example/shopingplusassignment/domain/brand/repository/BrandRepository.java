package com.example.shopingplusassignment.domain.brand.repository;

import com.example.shopingplusassignment.domain.brand.entity.Brand;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BrandRepository extends JpaRepository<Brand, Long> {

    Brand findByName(String name);

    @Query("SELECT b FROM Brand b JOIN FETCH b.seller WHERE b.id = :id")
    Optional<Brand> findByIdFetchSeller(@Param("id") Long id);

    @Query("SELECT b FROM Brand b JOIN FETCH b.productList WHERE b.id = :id")
    Optional<Brand> findByIdFetchProduct(@Param("id") Long brandId);

}
