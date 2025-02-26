package com.sparta.oishitable.domain.common.user.service;

import com.sparta.oishitable.domain.common.user.dto.request.UserProfileResponse;
import com.sparta.oishitable.domain.common.user.entity.User;
import com.sparta.oishitable.domain.common.user.repository.UserRepository;
import com.sparta.oishitable.domain.customer.follow.repository.FollowRepository;
import com.sparta.oishitable.global.exception.NotFoundException;
import com.sparta.oishitable.global.exception.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final FollowRepository followRepository;
    private final UserRepository userRepository;

    public UserProfileResponse findMyProfile(Long userId) {
        User user = findUserById(userId);

        long followerCount = followRepository.countFollower(userId);
        long followingCount = followRepository.countFollowing(userId);

        return UserProfileResponse.of(user, followerCount, followingCount);
    }

    public User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));
    }
}
