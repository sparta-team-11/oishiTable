package com.sparta.oishitable.domain.customer.post.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PostUpdateRequest(
        @NotNull
        Long regionId,

        @NotBlank
        String title,

        @NotBlank
        String content
) {
}
