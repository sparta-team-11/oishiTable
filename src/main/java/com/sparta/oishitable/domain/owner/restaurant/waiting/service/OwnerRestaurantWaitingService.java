package com.sparta.oishitable.domain.owner.restaurant.waiting.service;

import com.sparta.oishitable.domain.common.user.entity.User;
import com.sparta.oishitable.domain.common.user.repository.UserRepository;
import com.sparta.oishitable.domain.owner.restaurant.entity.Restaurant;
import com.sparta.oishitable.domain.owner.restaurant.service.OwnerRestaurantService;
import com.sparta.oishitable.domain.owner.restaurant.waiting.dto.response.WaitingQueueFindUsersResponse;
import com.sparta.oishitable.domain.owner.restaurant.waiting.dto.response.WaitingUserDetails;
import com.sparta.oishitable.domain.owner.restaurant.waiting.repository.OwnerWaitingRedisRepositoryImpl;
import com.sparta.oishitable.global.exception.ForbiddenException;
import com.sparta.oishitable.global.exception.NotFoundException;
import com.sparta.oishitable.global.exception.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OwnerRestaurantWaitingService {

    private final OwnerWaitingRedisRepositoryImpl ownerWaitingRedisRepository;
    private final OwnerRestaurantService ownerRestaurantService;
    private final UserRepository userRepository;

    public WaitingQueueFindUsersResponse findWaitingUsers(Long ownerId, Long restaurantId, int page, int size) {
        Restaurant restaurant = ownerRestaurantService.findById(restaurantId);
        checkRestaurantOwner(ownerId, restaurant.getOwner().getId());

        Long totalElements = ownerWaitingRedisRepository.findQueueSize(restaurant.getId());
        int totalPages = (int) Math.ceil((double) totalElements / size);

        List<Long> userIds = ownerWaitingRedisRepository.findWaitingUsers(restaurant.getId(), page, size);

        List<WaitingUserDetails> waitingUserDetails = Collections.emptyList();

        if (!userIds.isEmpty()) {
            waitingUserDetails = userRepository.findWaitingUserDetails(userIds);
        }

        return WaitingQueueFindUsersResponse.from(waitingUserDetails, page, totalPages, totalElements);
    }

    @Transactional
    public void updateWaitingStatus(Long ownerId, Long restaurantId) {
        Restaurant restaurant = ownerRestaurantService.findById(restaurantId);
        checkRestaurantOwner(ownerId, restaurant.getOwner().getId());

        restaurant.switchWaitingStatus();
    }

    public void clearWaitingQueue(Long ownerId, Long restaurantId) {
        Restaurant restaurant = ownerRestaurantService.findById(restaurantId);
        checkRestaurantOwner(ownerId, restaurant.getOwner().getId());

        ownerWaitingRedisRepository.clearWaitingQueue(restaurant.getId());
    }

    public void deleteUserFromWaitingQueue(Long ownerId, Long restaurantId, Long userId) {
        Restaurant restaurant = ownerRestaurantService.findById(restaurantId);
        checkRestaurantOwner(ownerId, restaurant.getOwner().getId());

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));

        Long deleteCount = ownerWaitingRedisRepository.deleteUserFromWaitingQueue(user.getId(), restaurant.getId());

        if (deleteCount == 0) {
            throw new NotFoundException(ErrorCode.WAITING_QUEUE_USER_NOT_FOUND);
        }
    }

    private void checkRestaurantOwner(Long ownerId, Long restaurantOwnerId) {
        if (!ownerId.equals(restaurantOwnerId)) {
            throw new ForbiddenException(ErrorCode.USER_UNAUTHORIZED);
        }
    }
}
