package com.sparta.oishitable.domain.comment.repository;

import com.sparta.oishitable.domain.comment.dto.response.CommentResponse;
import java.util.List;

public interface CommentRepositoryQuerydsl {

    List<CommentResponse> findPostComments(Long postId, Long cursorValue, int limit);
    List<CommentResponse> findReplies(Long parentCommentId, Long cursorValue, int limit);
}
