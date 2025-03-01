package com.sparta.oishitable.domain.customer.restaurant.waiting.service;

import com.sparta.oishitable.domain.common.user.entity.User;
import com.sparta.oishitable.domain.common.user.repository.UserRepository;
import com.sparta.oishitable.domain.customer.restaurant.waiting.dto.request.WaitingJoinRequest;
import com.sparta.oishitable.domain.customer.restaurant.waiting.dto.response.WaitingQueueFindSizeResponse;
import com.sparta.oishitable.domain.customer.restaurant.waiting.dto.response.WaitingQueueFindUserRankResponse;
import com.sparta.oishitable.domain.customer.restaurant.waiting.repository.CustomerWaitingRedisRepository;
import com.sparta.oishitable.domain.customer.restaurant.waiting.repository.CustomerWaitingRepository;
import com.sparta.oishitable.domain.owner.restaurant.entity.Restaurant;
import com.sparta.oishitable.domain.owner.restaurant.entity.WaitingStatus;
import com.sparta.oishitable.domain.owner.restaurant.service.OwnerRestaurantService;
import com.sparta.oishitable.domain.owner.restaurant.waiting.entity.WaitingRedisDto;
import com.sparta.oishitable.domain.owner.restaurant.waiting.entity.WaitingType;
import com.sparta.oishitable.global.exception.ConflictException;
import com.sparta.oishitable.global.exception.NotFoundException;
import com.sparta.oishitable.global.exception.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerRestaurantWaitingService {

    private final UserRepository userRepository;
    private final OwnerRestaurantService ownerRestaurantService;
    private final CustomerWaitingRepository customerWaitingRepository;
    private final CustomerWaitingRedisRepository customerWaitingRedisRepository;

    public void joinWaitingQueue(Long userId, Long restaurantId, WaitingJoinRequest waitingQueueCreateRequest) {
        Restaurant restaurant = ownerRestaurantService.findById(restaurantId);
        isPossibleWaiting(restaurant.getWaitingStatus());

        boolean isRegistered = customerWaitingRedisRepository.findUser(restaurantId, userId)
                .isPresent();

        if (isRegistered) {
            throw new ConflictException(ErrorCode.ALREADY_REGISTERED_USER_IN_WAITING_QUEUE);
        }

        User user = findUserById(userId);

        WaitingRedisDto waitingRedisDto = WaitingRedisDto.builder()
                .userId(user.getId())
                .totalCount(waitingQueueCreateRequest.totalCount())
                .waitingType(WaitingType.of(waitingQueueCreateRequest.waitingType()))
                .build();

        customerWaitingRedisRepository.push(restaurant.getId(), waitingRedisDto);
    }

    public void cancelWaitingQueue(Long userId, Long restaurantId) {
        Restaurant restaurant = ownerRestaurantService.findById(restaurantId);
        isPossibleWaiting(restaurant.getWaitingStatus());

        User user = findUserById(userId);

        Long rank = customerWaitingRedisRepository.findUserRank(user.getId(), restaurant.getId())
                .orElseThrow(() -> new NotFoundException(ErrorCode.WAITING_QUEUE_USER_NOT_FOUND));

        customerWaitingRedisRepository.remove(restaurant.getId(), rank);
    }

    public WaitingQueueFindUserRankResponse findWaitingQueueUserRank(Long userId, Long restaurantId) {
        Restaurant restaurant = ownerRestaurantService.findById(restaurantId);
        isPossibleWaiting(restaurant.getWaitingStatus());

        User user = findUserById(userId);

        Long rank = customerWaitingRedisRepository.findUserRank(user.getId(), restaurant.getId())
                .orElseThrow(() -> new NotFoundException(ErrorCode.WAITING_QUEUE_USER_NOT_FOUND));

        return WaitingQueueFindUserRankResponse.from(rank);
    }

    public WaitingQueueFindSizeResponse findWaitingQueueSize(Long restaurantId) {
        Restaurant restaurant = ownerRestaurantService.findById(restaurantId);
        isPossibleWaiting(restaurant.getWaitingStatus());

        Long size = customerWaitingRedisRepository.findQueueSize(restaurant.getId());

        return WaitingQueueFindSizeResponse.from(size);
    }

    private Integer getTodayLastSequence(Long restaurantId) {


        Integer todayMaxSequence = customerWaitingRepository.findTodayMaxSequence(restaurantId)
                .orElse(0);

        return todayMaxSequence;
    }

    private void findWaitingLastSequence() {

    }

    private void isPossibleWaiting(WaitingStatus waitingStatus) {
        if (waitingStatus == WaitingStatus.CLOSE) {
            throw new ConflictException(ErrorCode.RESTAURANT_WAITING_IS_CLOSED);
        }
    }

    private User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));
    }
}
