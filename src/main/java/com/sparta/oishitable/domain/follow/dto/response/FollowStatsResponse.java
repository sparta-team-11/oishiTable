package com.sparta.oishitable.domain.follow.dto.response;

import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record FollowStatsResponse(
        Long followerCount,
        Long followingCount,
        Boolean isFollowing
) {
}