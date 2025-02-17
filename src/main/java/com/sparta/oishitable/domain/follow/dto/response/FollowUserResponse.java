package com.sparta.oishitable.domain.follow.dto.response;

import com.sparta.oishitable.domain.user.entity.User;
import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record FollowUserResponse(Long userId, String name) {

    public static FollowUserResponse from(User user) {
        return FollowUserResponse.builder()
                .userId(user.getId())
                .name(user.getName())
                .build();
    }
}