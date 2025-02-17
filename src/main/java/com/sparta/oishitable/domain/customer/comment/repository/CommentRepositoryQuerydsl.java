package com.sparta.oishitable.domain.customer.comment.repository;

import com.sparta.oishitable.domain.customer.comment.dto.response.CommentPostResponse;
import com.sparta.oishitable.domain.customer.comment.dto.response.CommentRepliesResponse;
import com.sparta.oishitable.domain.customer.comment.entity.Comment;
import java.util.List;
import java.util.Optional;

public interface CommentRepositoryQuerydsl {

    List<CommentPostResponse> findPostComments(Long postId, Long cursorValue, int limit);
    List<CommentRepliesResponse> findReplies(Long parentCommentId, Long cursorValue, int limit);
    Optional<Comment> findCommentWithRepliesById(Long commentId);
}
