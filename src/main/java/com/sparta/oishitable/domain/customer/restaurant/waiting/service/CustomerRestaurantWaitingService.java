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

        User user = findUserById(userId);

        isExistsInWaiting(restaurant.getId(), user.getId(), WaitingType.IN);
        isExistsInWaiting(restaurant.getId(), user.getId(), WaitingType.OUT);

        WaitingType waitingType = WaitingType.of(waitingQueueCreateRequest.waitingType());
        String requestedWaitingKey = getWaitingKey(restaurant.getId(), waitingType);
        // TODO: 동시성 처리
        Integer sequence = findWaitingNextSequence(restaurant.getId(), waitingType);

        log.info("joined user daily sequence: {}", sequence);

        int totalCount = waitingQueueCreateRequest.totalCount();

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

        customerWaitingRepository.save(waiting);

        customerWaitingRedisRepository.zAdd(requestedWaitingKey, user.getId(), waiting.getDailySequence());
    }

    @Transactional
    public void cancelWaitingQueue(Long userId, Long restaurantId, Long waitingId) {
        Restaurant restaurant = ownerRestaurantService.findById(restaurantId);
        isPossibleWaiting(restaurant.getWaitingStatus());

        User user = findUserById(userId);
        Waiting waiting = findWaitingById(waitingId);

        authService.checkUserAuthority(waiting.getUser().getId(), user.getId());

        String key = getWaitingKey(waiting.getRestaurant().getId(), waiting.getType());

        customerWaitingRedisRepository.zFindUserRank(key, userId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.WAITING_QUEUE_USER_NOT_FOUND));

        waiting.updateStatus(ReservationStatus.CANCELED);

        customerWaitingRedisRepository.zRemove(key, user.getId());
    }

    public WaitingQueueFindUserRankResponse findWaitingQueueUserRank(Long userId, Long restaurantId, Long waitingId) {
        Restaurant restaurant = ownerRestaurantService.findById(restaurantId);
        isPossibleWaiting(restaurant.getWaitingStatus());

        User user = findUserById(userId);
        Waiting waiting = findWaitingById(waitingId);

        authService.checkUserAuthority(waiting.getUser().getId(), user.getId());

        String key = getWaitingKey(restaurant.getId(), waiting.getType());

        Long rank = customerWaitingRedisRepository.zFindUserRank(key, user.getId())
                .orElseThrow(() -> new NotFoundException(ErrorCode.WAITING_QUEUE_USER_NOT_FOUND));

        return WaitingQueueFindUserRankResponse.from(rank);
    }

    public WaitingQueueFindSizeResponse findWaitingQueueSize(Long restaurantId) {
        Restaurant restaurant = ownerRestaurantService.findById(restaurantId);
        isPossibleWaiting(restaurant.getWaitingStatus());

        String inRestaurantKey = getWaitingKey(restaurant.getId(), WaitingType.IN);
        Long inRestaurantWaitingSize = customerWaitingRedisRepository.zCard(inRestaurantKey);

        String takeOutKey = getWaitingKey(restaurant.getId(), WaitingType.OUT);
        Long takeOutWaitingSize = customerWaitingRedisRepository.zCard(takeOutKey);

        return WaitingQueueFindSizeResponse.from(inRestaurantWaitingSize, takeOutWaitingSize);
    }

    private void isExistsInWaiting(Long restaurantId, Long userId, WaitingType waitingType) {
        String inRestaurantKey = getWaitingKey(restaurantId, waitingType);
        boolean isExists = customerWaitingRedisRepository.zFindUserRank(inRestaurantKey, userId)
                .isPresent();

        if (isExists) {
            throw new ConflictException(ErrorCode.ALREADY_REGISTERED_USER_IN_WAITING_QUEUE);
        }
    }

    private Integer findWaitingNextSequence(Long restaurantId, WaitingType waitingType) {
        // 1.Redis 마지막 순번 조회
        String key = getWaitingKey(restaurantId, waitingType);
        Optional<Integer> sequence = customerWaitingRedisRepository.zFindLastSequence(key);

        if (sequence.isPresent()) {
            return sequence.get() + 1;
        }

        // 2. DB 마지막 순번 조회
        return customerWaitingRepository.findTodayLastSequence(restaurantId, waitingType)
                .orElse(0) + 1;
    }

    private String getWaitingKey(Long restaurantId, WaitingType waitingType) {
        return waitingType.getPrefix() + restaurantId;
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
