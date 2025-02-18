package com.sparta.oishitable.domain.customer.comment.controller;

import com.sparta.oishitable.domain.customer.comment.dto.request.CommentCreateRequest;
import com.sparta.oishitable.domain.customer.comment.dto.request.CommentUpdateRequest;
import com.sparta.oishitable.domain.customer.comment.dto.response.CommentPostResponse;
import com.sparta.oishitable.domain.customer.comment.dto.response.CommentRepliesResponse;
import com.sparta.oishitable.domain.customer.comment.service.CommentService;
import com.sparta.oishitable.global.security.entity.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/customer/api/posts/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<Void> create(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody @Valid CommentCreateRequest request

    ) {
        commentService.create(userDetails.getId(), request);

        return ResponseEntity.created(null).build();
    }

    @GetMapping
    public ResponseEntity<Slice<CommentPostResponse>> findPostComments(
            @RequestParam Long postId,
            @RequestParam(required = false) Long cursorValue,
            @RequestParam(defaultValue = "10") int limit
    ) {
        Slice<CommentPostResponse> comments = commentService.findPostComments(postId, cursorValue, limit);

        return ResponseEntity.ok(comments);
    }

    @GetMapping("/replies")
    public ResponseEntity<Page<CommentRepliesResponse>> findReplies(
            @RequestParam Long commentId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<CommentRepliesResponse> replies = commentService.findReplies(commentId, page, size);

        return ResponseEntity.ok(replies);
    }

    @PatchMapping("/{commentId}")
    public ResponseEntity<Void> updateComments(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long commentId,
            @RequestBody @Valid CommentUpdateRequest request
    ) {
        commentService.update(userDetails.getId(), commentId, request);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComments(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long commentId
    ) {
        commentService.delete(commentId, userDetails.getId());

        return ResponseEntity.noContent().build();
    }
}
