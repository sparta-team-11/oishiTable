package com.sparta.oishitable.domain.like.controller;

import com.sparta.oishitable.domain.like.service.CommentLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/comments/likes")
@RequiredArgsConstructor
public class CommentLikeController {

    private CommentLikeService commentLikeService;

    @PostMapping
    public ResponseEntity<Void> likeComment(
        @RequestParam Long commentId,
        @RequestParam Long userId
    ) {
        commentLikeService.likeComment(commentId, userId);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> unlikeComment(
        @RequestParam Long commentId,
        @RequestParam Long userId
    ) {
        commentLikeService.unlikeComment(commentId, userId);

        return ResponseEntity.noContent().build();
    }
}
