package com.sparta.oishitable.domain.customer.bookmark.dto.request;

import jakarta.validation.constraints.NotBlank;

public record BookmarkUpdateRequest(
        @NotBlank
        String updateMemo
) {
}
