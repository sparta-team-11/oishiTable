package com.sparta.oishitable.domain.follow.dto.response;

import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record FollowResponse(String message) {

    public static FollowResponse success() {
        return FollowResponse.builder()
                .message("팔로우 성공")
                .build();
    }

    public static FollowResponse unFollowed() {
        return FollowResponse.builder()
                .message("언팔로우 성공")
                .build();
    }
}