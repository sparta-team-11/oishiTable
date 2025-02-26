package com.sparta.oishitable.domain.customer.follow.repository;

public interface FollowRepositoryQuerydsl {

    long countFollowing(Long userId);

    long countFollower(Long userId);
}
