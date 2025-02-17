package com.sparta.oishitable.domain.follow.controller;

import com.sparta.oishitable.domain.follow.dto.response.FollowUserResponse;
import com.sparta.oishitable.domain.follow.service.FollowService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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

        return ResponseEntity.noContent().build();
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

    @GetMapping("/{userId}/followers/count")
    public ResponseEntity<Long> getFollowerCount(
            @AuthenticationPrincipal UserDetails userDetails
    ) {

        Long userId = Long.parseLong(userDetails.getUsername());

        return ResponseEntity.ok(followService.getFollowerCount(userId));
    }

    @GetMapping("/{userId}/followings/count")
    public ResponseEntity<Long> getFollowingCount(
            @AuthenticationPrincipal UserDetails userDetails
    ) {

        Long userId = Long.parseLong(userDetails.getUsername());
        
        return ResponseEntity.ok(followService.getFollowingCount(userId));
    }

    @GetMapping("/check/{followingId}")
    public ResponseEntity<Boolean> isFollowing(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long followingId
    ) {

        Long followerId = Long.parseLong(userDetails.getUsername());

        return ResponseEntity.ok(followService.isFollowing(followerId, followingId));
    }
}
