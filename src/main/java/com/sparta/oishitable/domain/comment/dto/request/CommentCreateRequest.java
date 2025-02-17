package com.sparta.oishitable.domain.comment.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CommentCreateRequest (
    @NotNull
    Long postId,

    @NotBlank
    String content,

    // 부모 댓글 아이디 (nullable)
    Long parentId
) {

}
