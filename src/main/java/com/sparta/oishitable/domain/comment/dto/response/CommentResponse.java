package com.sparta.oishitable.domain.comment.dto.response;

import com.sparta.oishitable.domain.comment.entity.Comment;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record CommentResponse (
    Long commentId,
    Long postId,
    Long userId,
    String username,
    String content,
    LocalDateTime modifiedAt
) {
    public static CommentResponse from(Comment comment) {
        return CommentResponse.builder()
            .commentId(comment.getId())
            .postId(comment.getPost().getId())
            .userId(comment.getUser().getId())
            .username(comment.getUser().getName())
            .content(comment.getContent())
            .modifiedAt(comment.getModifiedAt())
            .build();
    }

}
