package com.sparta.oishitable.domain.customer.bookmark.dto.request;

import jakarta.validation.constraints.NotNull;

public record BookmarkDeleteRequest(
        @NotNull
        Long restaurantId
) {
}
