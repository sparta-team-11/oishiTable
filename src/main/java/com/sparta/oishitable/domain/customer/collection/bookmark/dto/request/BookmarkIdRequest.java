package com.sparta.oishitable.domain.customer.collection.bookmark.dto.request;

import jakarta.validation.constraints.NotNull;

public record BookmarkIdRequest(
        @NotNull
        Long bookmarkId
) {
}
