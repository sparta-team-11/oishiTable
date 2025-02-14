package com.sparta.oishitable.domain.post.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PostCreateRequest (
    @NotNull
    Long userId,

    @NotNull
    Long regionId,

    @NotBlank
    String title,

    @NotBlank
    String content
) {


}
