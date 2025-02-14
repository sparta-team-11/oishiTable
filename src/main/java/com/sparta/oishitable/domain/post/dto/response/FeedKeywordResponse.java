package com.sparta.oishitable.domain.post.dto.response;

import java.util.List;

public record FeedKeywordResponse(
    List<PostKeywordResponse> posts,
    Long nextCursor,
    boolean hasMore
) {

}
