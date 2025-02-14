package com.sparta.oishitable.domain.comment.controller;

import com.sparta.oishitable.domain.comment.dto.request.CommentCreateRequest;
import com.sparta.oishitable.domain.comment.dto.response.CommentResponse;
import com.sparta.oishitable.domain.comment.service.CommentService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
        @RequestBody CommentCreateRequest request
    ) {
        commentService.create(request);
        return ResponseEntity.created(null).build();
    }

    @GetMapping
    public ResponseEntity<List<CommentResponse>> readPostComments(
        @RequestParam Long postId,
        @RequestParam(required = false) Long cursorValue,
        @RequestParam(defaultValue = "10") int limit
    ) {
        List<CommentResponse> comments = commentService.getPostComments(postId, cursorValue, limit);

        return ResponseEntity.ok(comments);
    }

    @GetMapping("/replies")
    public ResponseEntity<List<CommentResponse>> readReplies(
        @RequestParam Long commentId,
        @RequestParam(required = false) Long cursorValue,
        @RequestParam(defaultValue = "10") int limit
    ) {
        List<CommentResponse> replies = commentService.getReplies(commentId, cursorValue, limit);

        return ResponseEntity.ok(replies);
    }
}
