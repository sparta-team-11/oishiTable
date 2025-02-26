package com.sparta.oishitable.domain.common.user.dto.response;

import com.sparta.oishitable.domain.common.user.entity.User;
import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record UserMyProfileResponse(
        String userName,
        String userIntroduce,
        String region,
        long followerCount,
        long followingCount
) {

    public static UserMyProfileResponse of(User user, long followerCount, long followingCount) {
        String region = user.getRegion() != null ? user.getRegion().getName() : null;

        return UserMyProfileResponse.builder()
                .userName(user.getName())
                .userIntroduce(user.getIntroduce())
                .region(region)
                .followerCount(followerCount)
                .followingCount(followingCount)
                .build();
    }
}
