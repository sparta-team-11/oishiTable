package com.sparta.oishitable.domain.post.controller;

import com.sparta.oishitable.domain.post.dto.request.PostCreateRequest;
import com.sparta.oishitable.domain.post.dto.request.PostUpdateRequest;
import com.sparta.oishitable.domain.post.dto.response.FeedKeywordResponse;
import com.sparta.oishitable.domain.post.dto.response.FeedRandomResponse;
import com.sparta.oishitable.domain.post.service.PostService;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping
    public ResponseEntity<Void> create(
        @RequestBody PostCreateRequest request
    ) {
        postService.create(request);

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
        @PathVariable Long postId,
        @RequestBody PostUpdateRequest request
    ) {
        postService.update(postId, request);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> delete(
        @PathVariable Long postId,
        @RequestParam Long userId
    ) {
        postService.delete(postId, userId);

        return ResponseEntity.noContent().build();
    }
}
