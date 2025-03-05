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
    public void joinWaitingQueue(Long userId, Long restaurantId, WaitingJoinRequest request) {
        Restaurant restaurant = ownerRestaurantService.findById(restaurantId);
        isPossibleWaiting(restaurant.getWaitingStatus());

        User user = findUserById(userId);

        WaitingType waitingType = WaitingType.of(request.waitingType());
        String requestedWaitingKey = getWaitingKey(restaurant.getId(), waitingType);
        // TODO: 동시성 처리
        Integer sequence = findWaitingNextSequence(restaurant.getId(), waitingType);

        int totalCount = request.totalCount();

        if (waitingType.equals(WaitingType.OUT)) {
            totalCount = 1;
        }

        Waiting waiting = Waiting.builder()
                .user(user)
                .restaurant(restaurant)
                .dailySequence(sequence)
                .totalCount(totalCount)
                .type(waitingType)
                .status(ReservationStatus.RESERVED)
                .build();

        Boolean isAdded
                = customerWaitingRedisRepository.zAdd(requestedWaitingKey, user.getId(), waiting.getDailySequence());

        if (!isAdded) {
            log.error("user {} already registered in waiting queue", user.getId());
            throw new ConflictException(ErrorCode.ALREADY_REGISTERED_USER_IN_WAITING_QUEUE);
        }

        customerWaitingRepository.save(waiting);

        // 유저에게 대기열 등록에 성공함을 알리는 알림 전송 추가
    }

    @Transactional
    public void cancelWaitingQueue(Long userId, Long restaurantId, Long waitingId) {
        Restaurant restaurant = ownerRestaurantService.findById(restaurantId);
        isPossibleWaiting(restaurant.getWaitingStatus());

        Waiting waiting = findWaitingById(waitingId);

        User user = findUserById(userId);
        authService.checkUserAuthority(waiting.getUser().getId(), user.getId());

        if (waiting.getStatus().equals(ReservationStatus.COMPLETED)) {
            throw new ConflictException(ErrorCode.ALREADY_COMPLETED_WAITING_EXCEPTION);
        }

        if (waiting.getStatus().equals(ReservationStatus.CANCELED)) {
            throw new ConflictException(ErrorCode.ALREADY_CANCELED_WAITING_EXCEPTION);
        }

        String key = getWaitingKey(waiting.getRestaurant().getId(), waiting.getType());

        customerWaitingRedisRepository.zFindUserRank(key, userId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.WAITING_QUEUE_USER_NOT_FOUND));

        waiting.updateStatus(ReservationStatus.CANCELED);

        customerWaitingRedisRepository.zRemove(key, user.getId());

        // 유저에게 대기열 취소에 성공함을 알리는 알림 전송 추가
    }

    public WaitingQueueFindUserRankResponse findWaitingQueueUserRank(Long userId, Long restaurantId, Long waitingId) {
        Restaurant restaurant = ownerRestaurantService.findById(restaurantId);
        isPossibleWaiting(restaurant.getWaitingStatus());

        User user = findUserById(userId);
        Waiting waiting = findWaitingById(waitingId);

        authService.checkUserAuthority(waiting.getUser().getId(), user.getId());

        String key = waiting.getType().getWaitingKey(restaurant.getId());

        Long rank = customerWaitingRedisRepository.zFindUserRank(key, user.getId())
                .orElseThrow(() -> new NotFoundException(ErrorCode.WAITING_QUEUE_USER_NOT_FOUND));

        return WaitingQueueFindUserRankResponse.from(rank);
    }

    public WaitingQueueFindSizeResponse findWaitingQueueSize(Long restaurantId) {
        Restaurant restaurant = ownerRestaurantService.findById(restaurantId);
        isPossibleWaiting(restaurant.getWaitingStatus());

        String inRestaurantKey = WaitingType.IN.getWaitingKey(restaurant.getId());
        Long inRestaurantWaitingSize = customerWaitingRedisRepository.zCard(inRestaurantKey);

        String takeOutKey = WaitingType.OUT.getWaitingKey(restaurant.getId());
        Long takeOutWaitingSize = customerWaitingRedisRepository.zCard(takeOutKey);

        return WaitingQueueFindSizeResponse.from(inRestaurantWaitingSize, takeOutWaitingSize);
    }

    private Integer findWaitingNextSequence(Long restaurantId, WaitingType waitingType) {
        String key = waitingType.getWaitingKey(restaurantId);

        return customerWaitingRedisRepository.zFindLastSequence(key)
                .map(i -> i + 1)
                .orElseGet(() -> customerWaitingRepository.findTodayLastSequence(restaurantId, waitingType)
                        .orElse(1));
    }

    private void isPossibleWaiting(WaitingStatus waitingStatus) {
        if (waitingStatus.equals(WaitingStatus.CLOSE)) {
            throw new ConflictException(ErrorCode.RESTAURANT_WAITING_IS_CLOSED);
        }
    }

    private Waiting findWaitingById(Long waitingId) {
        return customerWaitingRepository.findById(waitingId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.WAITING_NOT_FOUND));
    }

    private User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));
    }
}
