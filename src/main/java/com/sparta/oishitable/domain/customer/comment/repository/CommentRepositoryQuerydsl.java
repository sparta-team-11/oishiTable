package com.sparta.oishitable.domain.customer.comment.repository;

import com.sparta.oishitable.domain.customer.comment.dto.response.CommentPostResponse;
import com.sparta.oishitable.domain.customer.comment.dto.response.CommentRepliesResponse;
import com.sparta.oishitable.domain.customer.comment.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface CommentRepositoryQuerydsl {

    List<CommentPostResponse> findPostComments(Long postId, Long cursorValue, int limit);
    Page<CommentRepliesResponse> findReplies(Long parentCommentId, Pageable pageable);
}
