package com.sparta.oishitable.domain.customer.follow.repository;

import com.sparta.oishitable.domain.customer.follow.entity.Follow;
import com.sparta.oishitable.domain.common.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Long>, FollowRepositoryQuerydsl {

    boolean existsByFollowerIdAndFollowingId(Long followerId, Long followingId);

    @Query("SELECT f.follower FROM Follow f WHERE f.following.id = :userId")
    Page<User> findFollowersByFollowingId(Long userId, Pageable pageable);

    @Query("SELECT f.following FROM Follow f WHERE f.follower.id = :userId")
    Page<User> findFollowingsByFollowerId(Long userId, Pageable pageable);

    Optional<Follow> findByFollowerIdAndFollowingId(Long followerId, Long followingId);
}