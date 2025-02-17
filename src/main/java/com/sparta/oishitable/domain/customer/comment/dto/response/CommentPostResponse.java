package com.sparta.oishitable.domain.customer.comment.dto.response;

import lombok.AccessLevel;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder(access = AccessLevel.PRIVATE)
public record CommentPostResponse(
        Long commentId,
        Long postId,
        Long userId,
        String username,
        String content,
        Long replyCount,
        Long likeCount,
        LocalDateTime modifiedAt
) {
}
