package com.sparta.oishitable.domain.follow.dto.response;

import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record FollowResponse(String message) {
}