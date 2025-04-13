package com.moonjin.realworld.user.dto.response;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.moonjin.realworld.user.domain.User;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ResponseBody;

import static com.fasterxml.jackson.annotation.JsonTypeInfo.As.WRAPPER_OBJECT;
import static com.fasterxml.jackson.annotation.JsonTypeInfo.Id.NAME;

@JsonTypeName("user")
@JsonTypeInfo(include = WRAPPER_OBJECT, use = NAME)
@Builder
@Getter
public class AuthResponse {
    private static final Logger log = LoggerFactory.getLogger(AuthResponse.class);
    String email;
    String username;
    String bio;
    String image;

    public static AuthResponse of(User user) {
        log.info("auth response: {}", user.getEmail());
        return AuthResponse.builder()
                .email(user.getEmail())
                .username(user.getUsername())
                .bio(user.getBio())
                .image(user.getImage())
                .build();
    }
}
