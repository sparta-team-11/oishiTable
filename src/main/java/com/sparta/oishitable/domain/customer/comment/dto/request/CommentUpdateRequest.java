package com.sparta.oishitable.domain.customer.comment.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CommentUpdateRequest(
        @NotBlank
        String content
) {
}
