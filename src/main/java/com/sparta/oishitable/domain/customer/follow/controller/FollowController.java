package com.sparta.oishitable.domain.customer.follow.controller;

import com.sparta.oishitable.domain.customer.follow.dto.FollowUserResponse;
import com.sparta.oishitable.domain.customer.follow.service.FollowService;
import com.sparta.oishitable.global.security.entity.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/customer/api/follows")
@RequiredArgsConstructor
public class FollowController {

    private final FollowService followService;

    @PostMapping("/{followingId}")
    public ResponseEntity<Void> followUser(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long followingId
    ) {
        followService.followUser(userDetails.getId(), followingId);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/{followingId}")
    public ResponseEntity<Void> unfollowUser(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long followingId
    ) {
        followService.unfollowUser(userDetails.getId(), followingId);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{userId}/followers")
    public ResponseEntity<Page<FollowUserResponse>> getFollowers(
            @PathVariable Long userId,
            @PageableDefault Pageable pageable
    ) {
        return ResponseEntity.ok(followService.getFollowers(userId, pageable));
    }

    @GetMapping("/{userId}/followings")
    public ResponseEntity<Page<FollowUserResponse>> getFollowings(
            @PathVariable Long userId,
            @PageableDefault Pageable pageable
    ) {
        return ResponseEntity.ok(followService.getFollowings(userId, pageable));
    }
}
