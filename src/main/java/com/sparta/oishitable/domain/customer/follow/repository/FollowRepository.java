package com.sparta.oishitable.domain.customer.follow.repository;

import com.sparta.oishitable.domain.customer.follow.entity.Follow;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Long>, FollowRepositoryQuerydsl {

    boolean existsByFollowerIdAndFollowingId(Long followerId, Long followingId);

    Optional<Follow> findByFollowerIdAndFollowingId(Long followerId, Long followingId);
}