package com.sparta.oishitable.domain.customer.restaurant.waiting.service;

import com.sparta.oishitable.domain.common.user.entity.User;
import com.sparta.oishitable.domain.common.user.repository.UserRepository;
import com.sparta.oishitable.domain.customer.reservation.entity.ReservationStatus;
import com.sparta.oishitable.domain.customer.restaurant.repository.CustomerRestaurantRepository;
import com.sparta.oishitable.domain.customer.restaurant.waiting.dto.request.WaitingJoinRequest;
import com.sparta.oishitable.domain.customer.restaurant.waiting.repository.CustomerWaitingRedisRepository;
import com.sparta.oishitable.domain.customer.restaurant.waiting.repository.CustomerWaitingRepository;
import com.sparta.oishitable.domain.owner.restaurant.entity.Restaurant;
import com.sparta.oishitable.domain.owner.restaurant.waiting.entity.Waiting;
import com.sparta.oishitable.domain.owner.restaurant.waiting.entity.WaitingType;
import com.sparta.oishitable.global.aop.annotation.DistributedLock;
import com.sparta.oishitable.global.exception.NotFoundException;
import com.sparta.oishitable.global.exception.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.ZoneId;

@Component
@RequiredArgsConstructor
public class CustomerRestaurantWaitingJoinService {

    private final UserRepository userRepository;
    private final CustomerWaitingRepository customerWaitingRepository;
    private final CustomerRestaurantRepository customerRestaurantRepository;
    private final CustomerWaitingRedisRepository customerWaitingRedisRepository;

    @DistributedLock(key = "'waiting:' + #restaurantId")
    public void joinWaitingQueue(Long userId, Long restaurantId, WaitingJoinRequest request) {
        Restaurant restaurant = findRestaurantById(restaurantId);
        User user = findUserById(userId);

        WaitingType waitingType = WaitingType.IN;
        String waitingKey = waitingType.getWaitingKey(restaurant.getId());

        Waiting waiting = Waiting.builder()
                .user(user)
                .restaurant(restaurant)
                .totalCount(request.totalCount())
                .type(waitingType)
                .status(ReservationStatus.RESERVED)
                .build();

        customerWaitingRepository.save(waiting);

        Instant requestedAt = waiting.getCreatedAt()
                .atZone(ZoneId.of("Asia/Seoul"))
                .toInstant();

        long epochSecond = requestedAt
                .getEpochSecond();

        int nano = requestedAt.getNano();

        double score = epochSecond + (nano / 1_000_000_000.0);

        customerWaitingRedisRepository.join(waitingKey, user.getId(), score);
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
