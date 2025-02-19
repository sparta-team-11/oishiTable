package com.sparta.oishitable.domain.customer.collection.bookmark.dto.request;

import java.util.List;

public record CollectionBookmarksCreateRequest(
        List<CollectionBookmarkCreateRequest> bookmarks
) {
}
