package com.sparta.oishitable.domain.like.controller;

import com.sparta.oishitable.domain.like.service.CommentLikeService;
import com.sparta.oishitable.global.security.entity.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/comments/likes")
@RequiredArgsConstructor
public class CommentLikeController {

    private final CommentLikeService commentLikeService;

    @PostMapping
    public ResponseEntity<Void> likeComment(
        @AuthenticationPrincipal CustomUserDetails user,
        @RequestParam Long commentId

    ) {
        commentLikeService.likeComment(commentId, user.getId());

        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> unlikeComment(
        @AuthenticationPrincipal CustomUserDetails user,
        @RequestParam Long commentId
    ) {
        commentLikeService.unlikeComment(commentId, user.getId());

        return ResponseEntity.noContent().build();
    }
}
