package com.moonjin.realworld.dto.response.article;

import com.moonjin.realworld.domain.article.Comment;
import com.moonjin.realworld.dto.response.user.Profile;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

public class CommentResponse {
    Long id;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    String body;
    Profile author;

    @Builder
    public CommentResponse(Long id, LocalDateTime createdAt, LocalDateTime updatedAt, String body, Profile author) {
        this.id = id;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.body = body;
    }

    public static CommentResponse of(Comment comment) {
        return CommentResponse.builder()
                .id(comment.getId())
                .createdAt(comment.getCreatedAt())
                .updatedAt(comment.getUpdatedAt())
                .body(comment.getBody())
                .author(Profile.of(comment.getAuthor(), false))
                .build();
    }
}
