package com.sparta.oishitable.domain.comment.service;

import com.sparta.oishitable.domain.comment.dto.request.CommentCreateRequest;
import com.sparta.oishitable.domain.comment.dto.response.CommentResponse;
import com.sparta.oishitable.domain.comment.entity.Comment;
import com.sparta.oishitable.domain.comment.repository.CommentRepository;
import com.sparta.oishitable.domain.post.entity.Post;
import com.sparta.oishitable.domain.post.repository.PostRepository;
import com.sparta.oishitable.domain.user.entity.User;
import com.sparta.oishitable.domain.user.repository.UserRepository;
import com.sparta.oishitable.global.exception.CustomRuntimeException;
import com.sparta.oishitable.global.exception.error.ErrorCode;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Transactional
    public void create(CommentCreateRequest request) {

        Post post = findPostById(request.postId());
        User user = findUserById(request.userId());

        // 부분적으로 빌드 후 게시글 댓글인지 대댓글인지 구분 후 조건에 따라 완성
        Comment.CommentBuilder builder = Comment.builder()
            .content(request.content())
            .user(user);

        if (request.parentId() != null) {
            // 대댓글 : 부모 댓글 조회 후 설정
            Comment parentComment = findCommentById(request.parentId());

            Comment comment = builder.parent(parentComment)
                .post(parentComment.getPost())
                .build();

            // 댓글의 대댓글 리스트에만 추가
            parentComment.addReply(comment);

            commentRepository.save(comment);

        } else {
            // 게시글 댓글
            Comment comment = builder.post(post)
                    .build();

            // 게시글의 댓글리스트에 추가
            post.addComment(comment);

            commentRepository.save(comment);
        }
    }

    public Slice<CommentResponse> getReplies(Long parentCommentId, Long cursorValue, int limit) {

        List<CommentResponse> replies = commentRepository.findReplies(
            parentCommentId,
            cursorValue,
            limit);

        boolean hasNext = replies.size() == limit;

        return new SliceImpl<>(replies, PageRequest.of(0, limit), hasNext);
    }

    public Slice<CommentResponse> getPostComments(Long postId, Long cursorValue, int limit) {

        List<CommentResponse> replies = commentRepository.findPostComments(
            postId,
            cursorValue,
            limit);

        boolean hasNext = replies.size() == limit;

        return new SliceImpl<>(replies, PageRequest.of(0, limit), hasNext);
    }


    // 헬퍼 메서드

    private User findUserById(Long userId) {
        return userRepository.findById(userId)
            .orElseThrow(() -> new CustomRuntimeException(ErrorCode.USER_NOT_FOUND));
    }

    private Post findPostById(Long postId) {
        return postRepository.findById(postId)
            .orElseThrow(() -> new CustomRuntimeException(ErrorCode.POST_NOT_FOUND));
    }

    private Comment findCommentById(Long commentId) {
        return commentRepository.findById(commentId)
            .orElseThrow(() -> new CustomRuntimeException(ErrorCode.COMMENT_NOT_FOUND));
    }

    private void isCommentOwner(Long commentUserId, Long userId) {
        if (!Objects.equals(commentUserId, userId)) {
            throw new CustomRuntimeException(ErrorCode.USER_UNAUTHORIZED);
        }
    }
}
