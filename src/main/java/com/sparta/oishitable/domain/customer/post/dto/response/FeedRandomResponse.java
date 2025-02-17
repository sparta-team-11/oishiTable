package com.sparta.oishitable.domain.customer.post.dto.response;

import java.util.List;

public record FeedRandomResponse(
        List<PostRandomResponse> posts,
        int randomSeed,
        Long nextCursor,
        boolean hasMore
) {
}
