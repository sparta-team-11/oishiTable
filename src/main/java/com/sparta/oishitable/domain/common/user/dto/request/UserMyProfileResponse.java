package com.sparta.oishitable.domain.common.user.dto.request;

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

    public UserMyProfileResponse of(User user, long followerCount, long followingCount) {
        return UserMyProfileResponse.builder()
                .userName(user.getName())
                .userIntroduce(user.getIntroduce())
                .region(user.getRegion().getName())
                .followerCount(followerCount)
                .followingCount(followingCount)
                .build();
    }
}
