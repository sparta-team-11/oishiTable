package com.sparta.oishitable.domain.customer.collection.dto.request;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CollectionUpdateRequest(
        @NotBlank
        String name,

        String description,

        @NotNull
        Boolean isPublic
) {
}
