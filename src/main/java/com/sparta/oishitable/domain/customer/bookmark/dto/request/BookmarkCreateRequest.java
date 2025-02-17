package com.sparta.oishitable.domain.customer.bookmark.dto.request;

import jakarta.validation.constraints.NotNull;

public record BookmarkCreateRequest(
        @NotNull
        Long restaurantId
) {
}
