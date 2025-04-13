package com.moonjin.realworld.user.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private String password;
    private String username;
    private String bio;
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
