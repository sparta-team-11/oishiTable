package com.sparta.oishitable.domain.customer.post.like.controller;

import com.sparta.oishitable.domain.customer.post.like.service.PostLikeService;
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
@RequestMapping("/api/posts/likes")
@RequiredArgsConstructor
public class PostLikeController {

    private final PostLikeService postLikeService;

    @PostMapping
    public ResponseEntity<Void> likePost(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam Long postId

    ) {
        postLikeService.likePost(postId, userDetails.getId());

        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> unlikePost(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam Long postId

    ) {
        postLikeService.unlikePost(postId, userDetails.getId());

        return ResponseEntity.noContent().build();
    }
}
