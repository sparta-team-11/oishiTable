package com.sparta.oishitable.domain.comment.controller;

import com.sparta.oishitable.domain.comment.dto.request.CommentCreateRequest;
import com.sparta.oishitable.domain.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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

}
