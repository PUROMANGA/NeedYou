package com.example.shopingplusassignment.domain.brand.entity;

import com.example.shopingplusassignment.base_entity.BaseEntity;
import com.example.shopingplusassignment.domain.seller.entity.Seller;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "brands")
@NoArgsConstructor
public class Brand extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id")
    private Seller seller;

    public static Brand create(String name, Seller seller) {
        Brand brand = new Brand();
        brand.name = name;
        brand.seller = seller;
        return brand;
    }

    public void update(String name) {
        this.name = name;
    }

}
