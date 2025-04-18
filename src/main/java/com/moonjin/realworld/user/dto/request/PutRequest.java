package com.moonjin.realworld.user.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
public class PutRequest {
    private String email;
    private String password;
    private String username;
    private String image;
    private String bio;

    @Builder
    public PutRequest(String email, String password, String username, String image, String bio) {
        this.email = email;
        this.password = password;
        this.username = username;
        this.image = image;
        this.bio = bio;
    }
}
