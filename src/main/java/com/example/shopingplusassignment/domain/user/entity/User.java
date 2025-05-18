package com.example.shopingplusassignment.domain.user.entity;

import base_entity.BaseEntity;
import com.example.shopingplusassignment.domain.user.enums.UserRole;
import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;
    private String name;
    private String password;
    @Column(unique = true)
    private String email;
    private String phone;
    @Enumerated(EnumType.STRING)
    private UserRole userRole;
    private boolean status;

}
