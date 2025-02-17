package com.sparta.oishitable.domain.customer.follow.dto;

import com.sparta.oishitable.domain.common.user.entity.User;
import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record FollowUserResponse(
        Long userId, String name
) {

    public static FollowUserResponse from(User user) {
        return FollowUserResponse.builder()
                .userId(user.getId())
                .name(user.getName())
                .build();
    }
}