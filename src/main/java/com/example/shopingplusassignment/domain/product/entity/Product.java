package com.example.shopingplusassignment.domain.product.entity;

import base_entity.BaseEntity;
import com.example.shopingplusassignment.domain.brand.entity.Brand;
import com.example.shopingplusassignment.domain.product.common.ProductCategory;
import com.example.shopingplusassignment.domain.product.dto.RequestProductDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "products")

public class Product extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private Long price;

    @Column(nullable = false)
    private Long stock;
    
    private Long rating;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProductCategory productCategory;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brands_id")
    private Brand brand;

    private Long sellerId;

    public Product(RequestProductDto requestProductDto, Brand brand, Long sellerId) {
        this.name = requestProductDto.getName();
        this.description = requestProductDto.getDescription();
        this.price = requestProductDto.getPrice();
        this.stock = requestProductDto.getStock();
        this.productCategory = requestProductDto.getProductCategory();
        this.brand = brand;
        this.sellerId = sellerId;
    }

    public void update(RequestProductDto requestProductDto) {
        this.name = requestProductDto.getName();
        this.description = requestProductDto.getDescription();
        this.price = requestProductDto.getPrice();
        this.stock = requestProductDto.getStock();
        this.productCategory = requestProductDto.getProductCategory();
    }

    /**
     * 브랜드까지 변경을 할 수 있도록 했습니다
     * @param requestProductDto
     */
    
    public void update(RequestProductDto requestProductDto, Brand brand) {
        this.name = requestProductDto.getName();
        this.description = requestProductDto.getDescription();
        this.price = requestProductDto.getPrice();
        this.stock = requestProductDto.getStock();
        this.productCategory = requestProductDto.getProductCategory();
        this.brand = brand;
    }
}
