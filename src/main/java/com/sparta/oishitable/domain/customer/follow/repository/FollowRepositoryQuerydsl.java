package com.sparta.oishitable.domain.customer.follow.repository;

import com.sparta.oishitable.domain.common.user.entity.User;

import java.util.List;

public interface FollowRepositoryQuerydsl {

    long countFollowing(Long userId);

    long countFollower(Long userId);

    List<User> findFollowersByFollowingId(Long userId, Long cursor, int limit);

    List<User> findFollowingsByFollowerId(Long userId, Long cursor, int limit);
}
