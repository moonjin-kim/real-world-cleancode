package com.moonjin.realworld.user.dto.response;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.moonjin.realworld.user.domain.User;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class Profile {
    String username;
    String bio;
    String image;
    Boolean following;

    public static Profile of(User user, boolean following) {
        return Profile.builder()
                .username(user.getUsername())
                .bio(user.getBio())
                .image(user.getImage())
                .following(following)
                .build();
    }
}
