package com.sparta.oishitable.domain.bookmark.dto.request;

import jakarta.validation.constraints.NotNull;

public record BookmarkDeleteRequest(
        @NotNull
        Long restaurantId
) {
}
