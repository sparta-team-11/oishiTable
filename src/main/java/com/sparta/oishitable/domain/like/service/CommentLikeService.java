package com.sparta.oishitable.domain.like.service;

import com.sparta.oishitable.domain.comment.entity.Comment;
import com.sparta.oishitable.domain.comment.repository.CommentRepository;
import com.sparta.oishitable.domain.like.entity.CommentLike;
import com.sparta.oishitable.domain.like.repository.CommentLikeRepository;
import com.sparta.oishitable.domain.user.entity.User;
import com.sparta.oishitable.domain.user.repository.UserRepository;
import com.sparta.oishitable.global.exception.CustomRuntimeException;
import com.sparta.oishitable.global.exception.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentLikeService {

    private final UserRepository userRepository;
    private final CommentLikeRepository commentLikeRepository;
    private final CommentRepository commentRepository;

    public void likeComment(Long commentId, Long userId) {

        Comment comment = findCommentById(commentId);
        User user = findUserById(userId);

        if (commentLikeRepository.existsByCommentAndUser(comment, user)) {
            throw new CustomRuntimeException(ErrorCode.LIKE_DUPLICATED);
        }

        CommentLike commentLike = CommentLike.builder()
            .comment(comment)
            .user(user)
            .build();

        commentLikeRepository.save(commentLike);
    }

    public void unlikeComment(Long commentId, Long userId) {

        Comment comment = findCommentById(commentId);
        User user = findUserById(userId);

        CommentLike commentLike = findCommentLikeByCommentAndUser(comment, user);

        commentLikeRepository.delete(commentLike);
    }

    private CommentLike findCommentLikeByCommentAndUser(Comment comment, User user) {
        return commentLikeRepository.findCommentLikeByCommentAndUser(comment, user)
            .orElseThrow(() -> new CustomRuntimeException(ErrorCode.LIKE_NOT_FOUND));
    }

    private User findUserById(Long userId) {
        return userRepository.findById(userId)
            .orElseThrow(() -> new CustomRuntimeException(ErrorCode.USER_NOT_FOUND));
    }

    private Comment findCommentById(Long commentId) {
        return commentRepository.findById(commentId)
            .orElseThrow(() -> new CustomRuntimeException(ErrorCode.COMMENT_NOT_FOUND));
    }
}