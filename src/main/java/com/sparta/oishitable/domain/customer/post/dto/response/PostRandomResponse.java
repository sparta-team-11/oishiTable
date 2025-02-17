package com.sparta.oishitable.domain.customer.post.dto.response;

import java.time.LocalDateTime;

public record PostRandomResponse(
        Long postId,
        Long userId,
        Long regionId,
        String title,
        String content,
        String username,
        LocalDateTime modifiedAt,
        Long randomValue
) {
}
