package com.moonjin.realworld.user.dto.response;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.moonjin.realworld.user.domain.User;
import lombok.Builder;
import lombok.Getter;

import static com.fasterxml.jackson.annotation.JsonTypeInfo.As.WRAPPER_OBJECT;
import static com.fasterxml.jackson.annotation.JsonTypeInfo.Id.NAME;

@JsonTypeName("profile")
@JsonTypeInfo(include = WRAPPER_OBJECT, use = NAME)
@Builder
@Getter
public class Profile {
    String username;
    String bio;
    String image;
    Boolean following;

    public static Profile of(User user) {
        return Profile.builder()
                .username(user.getUsername())
                .bio(user.getBio())
                .image(user.getImage())
                .build();
    }
}
