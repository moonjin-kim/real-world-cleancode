package com.moonjin.realworld.dto.response;

import com.moonjin.realworld.domain.user.User;
import lombok.Getter;

@Getter
public class Author {
    String username;
    String bio;
    String image;
    boolean favorited;

    public Author() {}

    public Author(String username, String bio, String image, boolean favorited) {
        this.username = username;
        this.bio = bio;
        this.image = image;
        this.favorited = favorited;
    }

    public Author(User author, boolean favorited) {
        this.username = author.getUsername();
        this.bio = author.getBio();
        this.image = author.getImage();
        this.favorited = favorited;
    }
}
