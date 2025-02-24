package com.sparta.oishitable.domain.customer.restaurant.waiting.service;

import com.sparta.oishitable.domain.common.user.entity.User;
import com.sparta.oishitable.domain.common.user.repository.UserRepository;
import com.sparta.oishitable.domain.customer.restaurant.waiting.dto.response.WaitingQueueSizeResponse;
import com.sparta.oishitable.domain.customer.restaurant.waiting.dto.response.WaitingQueueUserRankResponse;
import com.sparta.oishitable.domain.customer.restaurant.waiting.repository.CustomerRestaurantWaitingRepository;
import com.sparta.oishitable.domain.owner.restaurant.entity.Restaurant;
import com.sparta.oishitable.domain.owner.restaurant.entity.WaitingStatus;
import com.sparta.oishitable.domain.owner.restaurant.service.OwnerRestaurantService;
import com.sparta.oishitable.global.exception.ConflictException;
import com.sparta.oishitable.global.exception.NotFoundException;
import com.sparta.oishitable.global.exception.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerRestaurantWaitingService {

    private final UserRepository userRepository;
    private final OwnerRestaurantService ownerRestaurantService;
    private final CustomerRestaurantWaitingRepository customerRestaurantWaitingRepository;

    public void joinWaitingQueue(Long userId, Long restaurantId) {
        Restaurant restaurant = ownerRestaurantService.findById(restaurantId);
        checkWaitingStatus(restaurant.getWaitingStatus());

        User user = findUserById(userId);

        boolean isPresent = customerRestaurantWaitingRepository.findUserRank(user.getId(), restaurantId)
                .isPresent();

        if (isPresent) {
            throw new ConflictException(ErrorCode.ALREADY_REGISTERED_USER_IN_WAITING_QUEUE);
        }

        customerRestaurantWaitingRepository.push(user.getId(), restaurant.getId());
    }

    public void cancelWaitingQueue(Long userId, Long restaurantId) {
        Restaurant restaurant = ownerRestaurantService.findById(restaurantId);
        checkWaitingStatus(restaurant.getWaitingStatus());

        User user = findUserById(userId);

        customerRestaurantWaitingRepository.remove(user.getId(), restaurant.getId());
    }

    public WaitingQueueUserRankResponse findWaitingQueueUserRank(Long userId, Long restaurantId) {
        Restaurant restaurant = ownerRestaurantService.findById(restaurantId);
        checkWaitingStatus(restaurant.getWaitingStatus());

        User user = findUserById(userId);

        Long rank = customerRestaurantWaitingRepository.findUserRank(user.getId(), restaurant.getId())
                .orElseThrow(() -> new NotFoundException(ErrorCode.WAITING_QUEUE_USER_NOT_FOUND));

        return new WaitingQueueUserRankResponse(rank);
    }

    public WaitingQueueSizeResponse findWaitingQueueSize(Long restaurantId) {
        Restaurant restaurant = ownerRestaurantService.findById(restaurantId);
        checkWaitingStatus(restaurant.getWaitingStatus());

        Long size = customerRestaurantWaitingRepository.findQueueSize(restaurant.getId());

        return new WaitingQueueSizeResponse(size);
    }

    private void checkWaitingStatus(WaitingStatus waitingStatus) {
        if (waitingStatus == WaitingStatus.CLOSE) {
            throw new ConflictException(ErrorCode.RESTAURANT_WAITING_IS_CLOSED);
        }
    }

    private User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));
    }
}
