package com.sparta.oishitable.domain.customer.post.controller;

import com.sparta.oishitable.domain.customer.post.dto.request.PostCreateRequest;
import com.sparta.oishitable.domain.customer.post.dto.request.PostUpdateRequest;
import com.sparta.oishitable.domain.customer.post.dto.response.FeedKeywordResponse;
import com.sparta.oishitable.domain.customer.post.dto.response.FeedRandomResponse;
import com.sparta.oishitable.domain.customer.post.service.PostService;
import com.sparta.oishitable.global.security.entity.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Random;

@RestController
@RequestMapping("/customer/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping
    public ResponseEntity<Void> create(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody @Valid PostCreateRequest request
    ) {
        postService.create(userDetails.getId(), request);

        return ResponseEntity.created(null).build();
    }

    @GetMapping
    public ResponseEntity<FeedRandomResponse> readAllPosts(
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) Long regionId,
            @RequestParam(required = false) Long cursorValue, // 마지막 행의 랜덤 값
            @RequestParam(required = false) Integer randomSeed, // 클라이언트가 전달하는 이전 랜덤 시드값
            @RequestParam(defaultValue = "10") int limit
    ) {
        // 커서값이 null 이거나 랜덤시드가 전달되지 않았으면 새로운 랜덤시드 생성
        int seed = (cursorValue == null || randomSeed == null) ? new Random().nextInt() : randomSeed;

        FeedRandomResponse response = postService.getAllPosts(
                userId,
                regionId,
                cursorValue,
                limit,
                seed);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/keyword")
    public ResponseEntity<FeedKeywordResponse> readPostsByKeyword(
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) Long regionId,
            @RequestParam(required = false) Long cursorValue,
            @RequestParam String keyword,
            @RequestParam(defaultValue = "10") int limit
    ) {
        FeedKeywordResponse response = postService.getPostsByKeyword(
                userId,
                regionId,
                cursorValue,
                keyword,
                limit
        );

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{postId}")
    public ResponseEntity<Void> update(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long postId,
            @RequestBody @Valid PostUpdateRequest request
    ) {
        postService.update(userDetails.getId(), postId, request);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> delete(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long postId
    ) {
        postService.delete(postId, userDetails.getId());

        return ResponseEntity.noContent().build();
    }
}
