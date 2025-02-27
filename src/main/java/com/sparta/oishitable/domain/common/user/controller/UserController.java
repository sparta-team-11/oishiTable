package com.sparta.oishitable.domain.common.user.controller;

import com.sparta.oishitable.domain.common.user.dto.request.UserUpdateInfoRequest;
import com.sparta.oishitable.domain.common.user.dto.request.UserUpdateProfileRequest;
import com.sparta.oishitable.domain.common.user.dto.response.UserMyInfoResponse;
import com.sparta.oishitable.domain.common.user.dto.response.UserMyProfileResponse;
import com.sparta.oishitable.domain.common.user.dto.response.UserProfileResponse;
import com.sparta.oishitable.domain.common.user.service.UserService;
import com.sparta.oishitable.global.security.entity.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/customer/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<UserMyProfileResponse> findMyProfile(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        return ResponseEntity.ok(userService.findMyProfile(userDetails.getId()));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserProfileResponse> findUserProfile(
            @PathVariable Long userId
    ) {
        return ResponseEntity.ok(userService.findUserProfile(userId));
    }

    @GetMapping("/me/info")
    public ResponseEntity<UserMyInfoResponse> findMyInfo(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        return ResponseEntity.ok(userService.findMyInfo(userDetails.getId()));
    }

    @PatchMapping("/me/profile")
    public ResponseEntity<Void> updateMyProfile(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody @Valid UserUpdateProfileRequest request
    ) {
        userService.updateMyProfile(userDetails.getId(), request);

        return ResponseEntity.ok().build();
    }

    @PatchMapping("/me/info")
    public ResponseEntity<Void> updateMyProfile(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody @Valid UserUpdateInfoRequest request
    ) {
        userService.updateMyInfo(userDetails.getId(), request);

        return ResponseEntity.ok().build();
    }
}
