package com.sparta.oishitable.domain.comment.repository;

import com.sparta.oishitable.domain.comment.dto.response.CommentResponse;
import com.sparta.oishitable.domain.comment.entity.Comment;
import java.util.List;
import java.util.Optional;

public interface CommentRepositoryQuerydsl {

    List<CommentResponse> findPostComments(Long postId, Long cursorValue, int limit);
    List<CommentResponse> findReplies(Long parentCommentId, Long cursorValue, int limit);
    Optional<Comment> findCommentWithRepliesById(Long commentId);
}
