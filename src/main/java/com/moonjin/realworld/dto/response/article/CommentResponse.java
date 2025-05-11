package com.moonjin.realworld.dto.response.article;

import com.moonjin.realworld.domain.article.Comment;
import com.moonjin.realworld.dto.response.user.Profile;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
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
        this.author = author;
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

    public static CommentResponse of(Comment comment, Profile author) {
        return CommentResponse.builder()
                .id(comment.getId())
                .createdAt(comment.getCreatedAt())
                .updatedAt(comment.getUpdatedAt())
                .body(comment.getBody())
                .author(author)
                .build();
    }
}
