package com.moonjin.realworld.dto.response.user;

import com.moonjin.realworld.domain.user.User;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class Profile {
    String username;
    String bio;
    String image;
    Boolean following;

    public Profile(String username, String bio, String image, Boolean following) {
        this.username = username;
        this.bio = bio;
        this.image = image;
        this.following = following;
    }

    public static Profile of(User user, boolean following) {
        return Profile.builder()
                .username(user.getUsername())
                .bio(user.getBio())
                .image(user.getImage())
                .following(following)
                .build();
    }
}
