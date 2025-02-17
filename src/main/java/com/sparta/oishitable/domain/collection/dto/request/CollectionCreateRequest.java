package com.sparta.oishitable.domain.collection.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CollectionCreateRequest(
        @NotBlank
        String name,

        String description,

        @NotNull
        Boolean isPublic
) {
}
