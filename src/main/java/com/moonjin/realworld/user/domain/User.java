package com.moonjin.realworld.user.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50, nullable = false)
    private String email;
    @Column(length = 50,nullable = false)
    private String password;
    @Column(length = 10,nullable = false)
    private String username;
    @Column(nullable = true)
    private String bio;
    @Column(nullable = true)
    private String image;

    @Builder
    public User(String email, String password, String username, String bio, String image) {
        this.email = email;
        this.password = password;
        this.username = username;
        this.bio = bio;
        this.image = image;
    }
}
