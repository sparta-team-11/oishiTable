package com.sparta.oishitable.domain.common.user.dto.response;

import com.sparta.oishitable.domain.common.user.entity.User;
import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record UserProfileResponse(
        Long userId,
        String userName,
        String userIntroduce,
        String region,
        long followerCount,
        long followingCount
) {

    public static UserProfileResponse of(User user, long followerCount, long followingCount) {
        String region = user.getRegion() != null ? user.getRegion().getName() : null;

        return UserProfileResponse.builder()
                .userId(user.getId())
                .userName(user.getName())
                .userIntroduce(user.getIntroduce())
                .region(region)
                .followerCount(followerCount)
                .followingCount(followingCount)
                .build();
    }
}

