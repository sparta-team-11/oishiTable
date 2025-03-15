package com.sparta.oishitable.domain.customer.restaurant.waiting.service;

import com.sparta.oishitable.domain.common.user.entity.User;
import com.sparta.oishitable.domain.common.user.repository.UserRepository;
import com.sparta.oishitable.domain.customer.reservation.entity.ReservationStatus;
import com.sparta.oishitable.domain.customer.restaurant.repository.CustomerRestaurantRepository;
import com.sparta.oishitable.domain.customer.restaurant.waiting.dto.request.WaitingJoinRequest;
import com.sparta.oishitable.domain.customer.restaurant.waiting.repository.CustomerRestaurantWaitingRedisRepository;
import com.sparta.oishitable.domain.customer.restaurant.waiting.repository.CustomerRestaurantWaitingRepository;
import com.sparta.oishitable.domain.owner.restaurant.entity.Restaurant;
import com.sparta.oishitable.domain.owner.restaurant.waiting.entity.Waiting;
import com.sparta.oishitable.domain.owner.restaurant.waiting.entity.WaitingType;
import com.sparta.oishitable.global.aop.annotation.DistributedLock;
import com.sparta.oishitable.global.exception.NotFoundException;
import com.sparta.oishitable.global.exception.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomerRestaurantWaitingJoinService {

    private final UserRepository userRepository;
    private final CustomerRestaurantRepository customerRestaurantRepository;
    private final CustomerRestaurantWaitingRepository customerRestaurantWaitingRepository;
    private final CustomerRestaurantWaitingRedisRepository customerRestaurantWaitingRedisRepository;

    @DistributedLock(key = "'waiting:' + #restaurantId")
    public int joinWaitingQueue(Long userId, Long restaurantId, WaitingJoinRequest request) {
        Restaurant restaurant = findRestaurantById(restaurantId);
        User user = findUserById(userId);

        WaitingType waitingType = WaitingType.IN;
        Integer dailySequence = findWaitingNextSequence(restaurant.getId(), waitingType);

        Waiting waiting = Waiting.builder()
                .user(user)
                .restaurant(restaurant)
                .totalCount(request.totalCount())
                .dailySequence(dailySequence)
                .type(waitingType)
                .status(ReservationStatus.RESERVED)
                .build();

        customerRestaurantWaitingRepository.save(waiting);

        return dailySequence;
    }

    private Integer findWaitingNextSequence(Long restaurantId, WaitingType waitingType) {
        String waitingKey = waitingType.getWaitingKey(restaurantId);

        return customerRestaurantWaitingRedisRepository.zFindLastSequence(waitingKey)
                .map(i -> i + 1)
                .orElseGet(() -> customerRestaurantWaitingRepository.findTodayLastSequence(restaurantId, waitingType)
                        .map(i -> i + 1)
                        .orElse(1));
    }

    private User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));
    }

    private Restaurant findRestaurantById(Long restaurantId) {
        return customerRestaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.RESTAURANT_NOT_FOUND));
    }
}
