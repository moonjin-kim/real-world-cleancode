package com.moonjin.realworld.article.dto.response;

import com.moonjin.realworld.user.domain.User;

public class Author {
    String username;
    String bio;
    String image;
    boolean favorited;

    public Author(User author, boolean favorited) {
        this.username = author.getUsername();
        this.bio = author.getBio();
        this.image = author.getImage();
        this.favorited = favorited;
    }
}
