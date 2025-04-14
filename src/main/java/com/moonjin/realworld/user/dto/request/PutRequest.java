package com.moonjin.realworld.user.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PutRequest {
    private String email;
    private String password;
    private String username;
    private String image;
    private String bio;
}
