package com.sparta.oishitable.domain.customer.comment.dto.response;

import java.time.LocalDateTime;

public record CommentRepliesResponse(
        Long commentId,
        Long postId,
        Long userId,
        String username,
        String content,
        Long likeCount,
        LocalDateTime modifiedAt
) {
}
