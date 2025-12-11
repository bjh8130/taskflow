package com.example.taskflow.domain.user.entity;

import com.example.taskflow.common.entity.BaseEntity;
import com.example.taskflow.domain.user.enums.UserRole;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 20, unique = true, nullable = false)
    private String username;

    @Column(length = 50, unique = true, nullable = false)
    private String email;

    @Column(length = 50)
    private String name;

    @Column(length = 200)
    private String password;

    @Column(length = 20)
    @Enumerated(EnumType.STRING)
    private UserRole role = UserRole.USER;

    @Column
    private boolean isDeleted = false;

    public User(String username, String email, String name, String password) {
        this.username = username;
        this.email = email;
        this.name = name;
        this.password = password;
        this.role = UserRole.USER;
    }

    public void update(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public void softDelete() {
        this.isDeleted = true;
    }
}
