package com.moonjin.realworld.dto.request.article;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CommentCreate {
    @NotBlank(message = "댓글을 입력해주세요.")
    private String body;
}
