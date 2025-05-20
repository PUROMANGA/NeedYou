package com.example.shopingplusassignment.domain.brand.entity;

import com.example.shopingplusassignment.domain.seller.entity.Seller;
import com.example.shopingplusassignment.domain.user.entity.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
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

    @ManyToOne
    @JoinColumn(name = "seller_id")
    private Seller seller;

    public static Brand create(String name, Seller seller) {
        Brand brand = new Brand();
        brand.name = name;
        brand.seller = seller;
        return brand;
    }

}
