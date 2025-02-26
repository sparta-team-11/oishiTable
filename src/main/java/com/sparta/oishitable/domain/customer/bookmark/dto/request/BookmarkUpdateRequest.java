package com.sparta.oishitable.domain.customer.bookmark.dto.request;

import com.sparta.oishitable.domain.customer.bookmark.error.BookmarkErrorMessages;
import jakarta.validation.constraints.NotNull;

public record BookmarkUpdateRequest(
        @NotNull(message = BookmarkErrorMessages.MEMO_SHOULD_NOT_BE_NULL)
        String updateMemo
) {
}
