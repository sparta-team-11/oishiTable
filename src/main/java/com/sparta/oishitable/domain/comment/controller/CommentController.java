package com.sparta.oishitable.domain.comment.controller;

import com.sparta.oishitable.domain.comment.dto.request.CommentCreateRequest;
import com.sparta.oishitable.domain.comment.dto.request.CommentUpdateRequest;
import com.sparta.oishitable.domain.comment.dto.response.CommentPostResponse;
import com.sparta.oishitable.domain.comment.dto.response.CommentRepliesResponse;
import com.sparta.oishitable.domain.comment.service.CommentService;
import com.sparta.oishitable.global.security.entity.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/posts/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<Void> create(
        @AuthenticationPrincipal CustomUserDetails user,
        @RequestBody @Valid CommentCreateRequest request

    ) {
        commentService.create(user.getId(), request);

        return ResponseEntity.created(null).build();
    }

    @GetMapping
    public ResponseEntity<Slice<CommentPostResponse>> readPostComments(
        @RequestParam Long postId,
        @RequestParam(required = false) Long cursorValue,
        @RequestParam(defaultValue = "10") int limit
    ) {
        Slice<CommentPostResponse> comments = commentService.getPostComments(postId, cursorValue, limit);

        return ResponseEntity.ok(comments);
    }

    @GetMapping("/replies")
    public ResponseEntity<Slice<CommentRepliesResponse>> readReplies(
        @RequestParam Long commentId,
        @RequestParam(required = false) Long cursorValue,
        @RequestParam(defaultValue = "10") int limit
    ) {
        Slice<CommentRepliesResponse> replies = commentService.getReplies(commentId, cursorValue, limit);

        return ResponseEntity.ok(replies);
    }

    @PatchMapping("/{commentId}")
    public ResponseEntity<Void> update(
        @AuthenticationPrincipal CustomUserDetails user,
        @PathVariable Long commentId,
        @RequestBody @Valid CommentUpdateRequest request
    ) {
        commentService.update(user.getId(), commentId, request);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> delete(
        @AuthenticationPrincipal CustomUserDetails user,
        @PathVariable Long commentId
    ) {
        commentService.delete(commentId, user.getId());

        return ResponseEntity.noContent().build();
    }

}
