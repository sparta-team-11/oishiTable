package com.sparta.oishitable.domain.common.user.dto.request;

import com.sparta.oishitable.domain.common.user.entity.User;
import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record UserProfileResponse(
        String userName,
        String userIntroduce,
        String region,
        long followerCount,
        long followingCount
) {

    public UserProfileResponse of(User user, long followerCount, long followingCount) {
        return UserProfileResponse.builder()
                .userName(user.getName())
                .userIntroduce(user.getIntroduce())
                .region(user.getRegion().getName())
                .followerCount(followerCount)
                .followingCount(followingCount)
                .build();
    }
}
