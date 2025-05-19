package com.example.shopingplusassignment.domain.brand.entity;

import com.example.shopingplusassignment.domain.seller.entity.Seller;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "brands")
@NoArgsConstructor
public class Brand {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private Long bookmarkCount;

    @ManyToOne
    @JoinColumn(name = "seller_id")
    private Seller seller;

}
