package com.example.shopingplusassignment.domain.user.entity;

import com.example.shopingplusassignment.base_entity.BaseEntity;
import com.example.shopingplusassignment.domain.user.enums.UserRole;
import jakarta.persistence.*;
import lombok.Builder;
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
    @Column(unique = true)
    private String email;
    private String password;
    private String phone;
    @Enumerated(EnumType.STRING)
    private UserRole userRole;
    @Column(nullable = false)
    private boolean status = false;

    @Builder
    public User(String name, String email, String password, String phone, UserRole userRole) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.userRole = userRole;
    }

    public void userUpdate(String name, String phone){
        this.name = name;
        this.phone = phone;
    }

    public void updatePassword(String newPassword){
        this.password = newPassword;
    }
}
