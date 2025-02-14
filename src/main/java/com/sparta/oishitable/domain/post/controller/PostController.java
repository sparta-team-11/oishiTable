package com.sparta.oishitable.domain.post.controller;

import com.sparta.oishitable.domain.post.dto.request.PostCreateRequest;
import com.sparta.oishitable.domain.post.dto.request.PostUpdateRequest;
import com.sparta.oishitable.domain.post.dto.response.PostResponse;
import com.sparta.oishitable.domain.post.service.PostService;
import java.util.List;
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
    public ResponseEntity<List<PostResponse>> readAllPosts(
        @RequestParam(required = false) Long userId,
        @RequestParam(required = false) Long regionId,
        @RequestParam(required = false) Long cursorId,
        @RequestParam(defaultValue = "10") int limit
    ) {
        List<PostResponse> posts = postService.getAllPosts(userId, regionId, cursorId, limit);

        return ResponseEntity.ok(posts);
    }

    @GetMapping("/keyword")
    public ResponseEntity<List<PostResponse>> readPostsByKeyword(
        @RequestParam(required = false) Long userId,
        @RequestParam(required = false) Long regionId,
        @RequestParam(required = false) Long cursorId,
        @RequestParam String keyword,
        @RequestParam(defaultValue = "10") int limit
    ) {
        List<PostResponse> posts = postService.getPostsByKeyword(
            userId,
            regionId,
            cursorId,
            keyword,
            limit
        );

        return ResponseEntity.ok(posts);
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
