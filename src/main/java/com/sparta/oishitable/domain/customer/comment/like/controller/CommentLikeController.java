package com.sparta.oishitable.domain.customer.comment.like.controller;

import com.sparta.oishitable.domain.customer.comment.like.service.CommentLikeService;
import com.sparta.oishitable.global.security.entity.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/customer/api/comments/likes")
@RequiredArgsConstructor
public class CommentLikeController {

    private final CommentLikeService commentLikeService;

    @PostMapping
    public ResponseEntity<Void> likeComment(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam Long commentId
    ) {
        commentLikeService.likeComment(commentId, userDetails.getId());

        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> unlikeComment(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam Long commentId
    ) {
        commentLikeService.unlikeComment(commentId, userDetails.getId());

        return ResponseEntity.noContent().build();
    }
}
