package com.sparta.oishitable.domain.common.user.service;

import com.sparta.oishitable.domain.common.user.dto.request.UserUpdateProfileRequest;
import com.sparta.oishitable.domain.common.user.dto.response.UserMyProfileResponse;
import com.sparta.oishitable.domain.common.user.dto.response.UserProfileResponse;
import com.sparta.oishitable.domain.common.user.entity.User;
import com.sparta.oishitable.domain.common.user.repository.UserRepository;
import com.sparta.oishitable.domain.customer.follow.repository.FollowRepository;
import com.sparta.oishitable.domain.customer.post.region.entity.Region;
import com.sparta.oishitable.domain.customer.post.region.repository.RegionRepository;
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
    private final RegionRepository regionRepository;

    public UserMyProfileResponse findMyProfile(Long userId) {
        User user = findUserById(userId);

        long followerCount = followRepository.countFollower(userId);
        long followingCount = followRepository.countFollowing(userId);

        return UserMyProfileResponse.of(user, followerCount, followingCount);
    }

    public UserProfileResponse findUserProfile(Long userId) {
        User user = findUserById(userId);

        long followerCount = followRepository.countFollower(userId);
        long followingCount = followRepository.countFollowing(userId);

        return UserProfileResponse.of(user, followerCount, followingCount);
    }

    public void updateMyProfile(Long userId, UserUpdateProfileRequest request) {
        User user = findUserById(userId);

        Region region = request.regionId() != null ?
                regionRepository.findById(request.regionId()).orElse(null) : null;

        user.updateProfile(request.nickname(), request.introduce(), region);
    }

    public User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));
    }
}
