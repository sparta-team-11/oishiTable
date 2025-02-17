package com.sparta.oishitable.domain.customer.follow.controller;

import com.sparta.oishitable.domain.customer.follow.dto.FollowUserResponse;
import com.sparta.oishitable.domain.customer.follow.service.FollowService;
import com.sparta.oishitable.global.security.entity.CustomUserDetails;
import com.sparta.oishitable.global.util.UriBuilderUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/customer/api/follows")
@RequiredArgsConstructor
public class FollowController {

    private final FollowService followService;

    @PostMapping("/{followingId}")
    public ResponseEntity<Void> createFollow(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long followingId
    ) {
        Long followId = followService.createFollow(userDetails.getId(), followingId);

        URI location = UriBuilderUtil.create("/customer/api/follows", followId);

        return ResponseEntity.created(location).build();
    }

    @DeleteMapping("/{followingId}")
    public ResponseEntity<Void> deleteUnfollow(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long followingId
    ) {
        followService.deleteUnfollow(userDetails.getId(), followingId);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{userId}/followers")
    public ResponseEntity<Page<FollowUserResponse>> findFollowers(
            @PathVariable Long userId,
            @PageableDefault Pageable pageable
    ) {
        return ResponseEntity.ok(followService.findFollowers(userId, pageable));
    }

    @GetMapping("/{userId}/followings")
    public ResponseEntity<Page<FollowUserResponse>> findFollowings(
            @PathVariable Long userId,
            @PageableDefault Pageable pageable
    ) {
        return ResponseEntity.ok(followService.findFollowings(userId, pageable));
    }
}
