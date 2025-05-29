package com.example.shopingplusassignment.domain.seller.entity;

import com.example.shopingplusassignment.base_entity.BaseEntity;
import com.example.shopingplusassignment.domain.brand.entity.Brand;
import com.example.shopingplusassignment.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "sellers")
@NoArgsConstructor
public class Seller extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name; // 업체명

    private String ceoName; // 대표자명

    @Column(unique = true)
    private String email; // 사업자 이메일

    @Column(unique = true)
    private String businessNumber; // 사업자 등록번호

    private String businessAddress; // 사업장 소재지

    private String customerServiceNumber; // 고객센터 전화번호

    @OneToMany(mappedBy = "seller", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Brand> brands = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    public Seller(String name, String ceoName, String email, String businessNumber, String businessAddress, String customerServiceNumber, User user) {
        this.name = name;
        this.ceoName = ceoName;
        this.email = email;
        this.businessNumber = businessNumber;
        this.businessAddress = businessAddress;
        this.customerServiceNumber = customerServiceNumber;
        this.user = user;
    }

    public static Seller createSeller(String name, String ceoName, String email, String businessNumber, String businessAddress, String customerServiceNumber, User user) {
        return new Seller(name, ceoName, email, businessNumber, businessAddress, customerServiceNumber, user);
    }

    public void update(String name, String ceoName, String email, String businessNumber, String businessAddress, String customerServiceNumber) {
        this.name = name;
        this.ceoName = ceoName;
        this.email = email;
        this.businessNumber = businessNumber;
        this.businessAddress = businessAddress;
        this.customerServiceNumber = customerServiceNumber;
    }
}
