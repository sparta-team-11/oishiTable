package com.sparta.oishitable.domain.customer.bookmark.dto.request;

import com.sparta.oishitable.domain.customer.bookmark.error.BookmarkErrorMessages;
import jakarta.validation.constraints.NotNull;

public record BookmarkCreateRequest(
        @NotNull(message = BookmarkErrorMessages.RESTAURANT_ID_SHOULD_NOT_BE_NULL)
        Long restaurantId
) {
}
