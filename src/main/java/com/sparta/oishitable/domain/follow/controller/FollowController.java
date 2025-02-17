package com.sparta.oishitable.domain.follow.controller;

import com.sparta.oishitable.domain.follow.dto.FollowUserResponse;
import com.sparta.oishitable.domain.follow.service.FollowService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/follows")
@RequiredArgsConstructor
public class FollowController {

    private final FollowService followService;

    @PostMapping("/{followingId}")
    public ResponseEntity<Void> followUser(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long followingId
    ) {

        Long followerId = Long.parseLong(userDetails.getUsername());

        followService.followUser(followerId, followingId);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/{followingId}")
    public ResponseEntity<Void> unfollowUser(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long followingId
    ) {

        Long followerId = Long.parseLong(userDetails.getUsername());

        followService.unfollowUser(followerId, followingId);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{userId}/followers")
    public ResponseEntity<Page<FollowUserResponse>> getFollowers(
            @PathVariable Long userId,
            @PageableDefault(size = 10) Pageable pageable
    ) {

        return ResponseEntity.ok(followService.getFollowers(userId, pageable));
    }

    @GetMapping("/{userId}/followings")
    public ResponseEntity<Page<FollowUserResponse>> getFollowings(
            @PathVariable Long userId,
            @PageableDefault(size = 10) Pageable pageable
    ) {

        return ResponseEntity.ok(followService.getFollowings(userId, pageable));
    }
}
