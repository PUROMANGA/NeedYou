package com.example.shopingplusassignment.domain.user.entity;

import base_entity.BaseEntity;
import com.example.shopingplusassignment.domain.user.enums.UserRole;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "users")
@NoArgsConstructor
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String password;
    @Column(unique = true)
    private String email;
    private String phone;
    @Enumerated(EnumType.STRING)
    private UserRole userRole;
    @Column(nullable = false)
    private boolean status = false;

    public User(String name, String password, String email, String phone, UserRole userRole) {
        this.name = name;
        this.password = password;
        this.email = email;
        this.phone = phone;
        this.userRole = userRole;
    }

}
