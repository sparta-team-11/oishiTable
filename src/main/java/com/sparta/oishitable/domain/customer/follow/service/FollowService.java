package com.sparta.oishitable.domain.customer.follow.service;

import com.sparta.oishitable.domain.common.user.entity.User;
import com.sparta.oishitable.domain.common.user.repository.UserRepository;
import com.sparta.oishitable.domain.customer.follow.dto.FollowUserResponse;
import com.sparta.oishitable.domain.customer.follow.entity.Follow;
import com.sparta.oishitable.domain.customer.follow.repository.FollowRepository;
import com.sparta.oishitable.global.exception.CustomRuntimeException;
import com.sparta.oishitable.global.exception.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FollowService {

    private final FollowRepository followRepository;
    private final UserRepository userRepository;

    @Transactional
    public Long createFollow(Long followerId, Long followingId) {
        if (followerId.equals(followingId)) {
            throw new CustomRuntimeException(ErrorCode.CANNOT_FOLLOW_SELF);
        }

        if (followRepository.existsByFollowerIdAndFollowingId(followerId, followingId)) {
            throw new CustomRuntimeException(ErrorCode.ALREADY_FOLLOWING);
        }

        User follower = userRepository.findById(followerId)
                .orElseThrow(() -> new CustomRuntimeException(ErrorCode.USER_NOT_FOUND));

        User following = userRepository.findById(followingId)
                .orElseThrow(() -> new CustomRuntimeException(ErrorCode.USER_NOT_FOUND));

        Follow follow = Follow.builder()
                .follower(follower)
                .following(following)
                .build();

        followRepository.save(follow);

        return follow.getId();
    }

    @Transactional
    public void deleteUnfollow(Long followerId, Long followingId) {
        Follow follow = followRepository.findByFollowerIdAndFollowingId(followerId, followingId)
                .orElseThrow(() -> new CustomRuntimeException(ErrorCode.FOLLOW_NOT_FOUND));

        followRepository.delete(follow);
    }

    public Page<FollowUserResponse> findFollowers(Long userId, Pageable pageable) {
        return followRepository.findFollowersByFollowingId(userId, pageable)
                .map(FollowUserResponse::from);
    }

    public Page<FollowUserResponse> findFollowings(Long userId, Pageable pageable) {
        return followRepository.findFollowingsByFollowerId(userId, pageable)
                .map(FollowUserResponse::from);
    }
}