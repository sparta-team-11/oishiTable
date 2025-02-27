package com.sparta.oishitable.domain.customer.collection.bookmark.dto.request;

import com.sparta.oishitable.domain.customer.collection.bookmark.error.CollectionBookmarkErrorMessage;
import jakarta.validation.constraints.NotNull;

public record CollectionBookmarkCreateRequest(
        @NotNull(message = CollectionBookmarkErrorMessage.BOOKMARK_ID_SHOULD_NOT_BE_NULL)
        Long bookmarkId
) {
}
