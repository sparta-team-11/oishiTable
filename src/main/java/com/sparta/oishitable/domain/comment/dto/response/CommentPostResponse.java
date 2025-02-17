package com.sparta.oishitable.domain.comment.dto.response;

import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;

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
