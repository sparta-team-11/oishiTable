package com.sparta.oishitable.domain.customer.restaurant.waiting.service;

import com.sparta.oishitable.domain.common.auth.service.AuthService;
import com.sparta.oishitable.domain.common.user.entity.User;
import com.sparta.oishitable.domain.common.user.repository.UserRepository;
import com.sparta.oishitable.domain.customer.reservation.entity.ReservationStatus;
import com.sparta.oishitable.domain.customer.restaurant.waiting.dto.request.WaitingJoinRequest;
import com.sparta.oishitable.domain.customer.restaurant.waiting.dto.response.WaitingQueueFindSizeResponse;
import com.sparta.oishitable.domain.customer.restaurant.waiting.dto.response.WaitingQueueFindUserRankResponse;
import com.sparta.oishitable.domain.customer.restaurant.waiting.repository.CustomerWaitingRedisRepository;
import com.sparta.oishitable.domain.customer.restaurant.waiting.repository.CustomerWaitingRepository;
import com.sparta.oishitable.domain.owner.restaurant.entity.Restaurant;
import com.sparta.oishitable.domain.owner.restaurant.entity.WaitingStatus;
import com.sparta.oishitable.domain.owner.restaurant.service.OwnerRestaurantService;
import com.sparta.oishitable.domain.owner.restaurant.waiting.entity.Waiting;
import com.sparta.oishitable.domain.owner.restaurant.waiting.entity.WaitingType;
import com.sparta.oishitable.global.exception.ConflictException;
import com.sparta.oishitable.global.exception.NotFoundException;
import com.sparta.oishitable.global.exception.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerRestaurantWaitingService {

    private final AuthService authService;
    private final UserRepository userRepository;
    private final OwnerRestaurantService ownerRestaurantService;
    private final CustomerWaitingRepository customerWaitingRepository;
    private final CustomerWaitingRedisRepository customerWaitingRedisRepository;

    @Transactional
    public void joinWaitingQueue(Long userId, Long restaurantId, WaitingJoinRequest waitingQueueCreateRequest) {
        Restaurant restaurant = ownerRestaurantService.findById(restaurantId);
        isPossibleWaiting(restaurant.getWaitingStatus());

        boolean isRegistered = customerWaitingRedisRepository.zFindUserRank(restaurantId, userId)
                .isPresent();

        if (isRegistered) {
            throw new ConflictException(ErrorCode.ALREADY_REGISTERED_USER_IN_WAITING_QUEUE);
        }

        User user = findUserById(userId);

        Integer lastSequence = findWaitingLastSequence(restaurant.getId());
        Integer sequence = lastSequence + 1;
        log.info("joined user daily sequence: {}", sequence);

        Waiting waiting = Waiting.builder()
                .user(user)
                .restaurant(restaurant)
                .dailySequence(sequence)
                .totalCount(waitingQueueCreateRequest.totalCount())
                .type(WaitingType.of(waitingQueueCreateRequest.waitingType()))
                .status(ReservationStatus.RESERVED)
                .build();

        customerWaitingRepository.save(waiting);

        customerWaitingRedisRepository.zAdd(restaurant.getId(), user.getId(), sequence);
    }

    @Transactional
    public void cancelWaitingQueue(Long userId, Long restaurantId, Long waitingId) {
        Restaurant restaurant = ownerRestaurantService.findById(restaurantId);
        isPossibleWaiting(restaurant.getWaitingStatus());

        User user = findUserById(userId);

        customerWaitingRedisRepository.zFindUserRank(restaurantId, userId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.WAITING_QUEUE_USER_NOT_FOUND));

        Waiting waiting = customerWaitingRepository.findById(waitingId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.WAITING_NOT_FOUND));

        authService.checkUserAuthority(waiting.getUser().getId(), user.getId());

        waiting.updateStatus(ReservationStatus.CANCELED);

        customerWaitingRedisRepository.zRemove(restaurant.getId(), user.getId());
    }

    private Integer findWaitingLastSequence(Long restaurantId) {
        // 1.Redis 마지막 순번 조회
        Optional<Integer> sequence = customerWaitingRedisRepository.zFindLastSequence(restaurantId);

        if (sequence.isPresent()) {
            return sequence.get();
        }

        // 2. DB 마지막 순번 조회
        return customerWaitingRepository.findTodayLastSequence(restaurantId)
                .orElse(0);
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
