package com.sparta.oishitable.domain.follow.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record FollowRequest(@NotNull Long targetUserId) {
}
