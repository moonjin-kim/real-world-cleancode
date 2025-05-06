package com.moonjin.realworld.dto.request.user;

import jakarta.validation.constraints.Email;
import lombok.Builder;
import lombok.Data;

@Data
public class PutUser {
    @Email
    private String email;
    private String password;
    private String username;
    private String image;
    private String bio;

    @Builder
    public PutUser(String email, String password, String username, String image, String bio) {
        this.email = email;
        this.password = password;
        this.username = username;
        this.image = image;
        this.bio = bio;
    }
}
