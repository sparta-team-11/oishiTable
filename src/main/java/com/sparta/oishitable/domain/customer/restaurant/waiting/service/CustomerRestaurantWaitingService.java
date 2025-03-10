package com.sparta.oishitable.domain.customer.restaurant.waiting.service;

import com.sparta.oishitable.domain.common.auth.service.AuthService;
import com.sparta.oishitable.domain.common.user.entity.User;
import com.sparta.oishitable.domain.common.user.repository.UserRepository;
import com.sparta.oishitable.domain.customer.reservation.entity.ReservationStatus;
import com.sparta.oishitable.domain.customer.restaurant.repository.CustomerRestaurantRepository;
import com.sparta.oishitable.domain.customer.restaurant.waiting.dto.request.WaitingJoinRequest;
import com.sparta.oishitable.domain.customer.restaurant.waiting.dto.response.WaitingQueueFindSizeResponse;
import com.sparta.oishitable.domain.customer.restaurant.waiting.dto.response.WaitingQueueFindUserRankResponse;
import com.sparta.oishitable.domain.customer.restaurant.waiting.repository.CustomerWaitingRedisRepository;
import com.sparta.oishitable.domain.customer.restaurant.waiting.repository.CustomerWaitingRepository;
import com.sparta.oishitable.domain.owner.restaurant.entity.Restaurant;
import com.sparta.oishitable.domain.owner.restaurant.entity.WaitingStatus;
import com.sparta.oishitable.domain.owner.restaurant.waiting.entity.Waiting;
import com.sparta.oishitable.domain.owner.restaurant.waiting.entity.WaitingType;
import com.sparta.oishitable.global.aop.annotation.DistributedLock;
import com.sparta.oishitable.global.exception.ConflictException;
import com.sparta.oishitable.global.exception.NotFoundException;
import com.sparta.oishitable.global.exception.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.ZoneId;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerRestaurantWaitingService {

    private final AuthService authService;
    private final UserRepository userRepository;
    private final CustomerWaitingRepository customerWaitingRepository;
    private final CustomerRestaurantRepository customerRestaurantRepository;
    private final CustomerWaitingRedisRepository customerWaitingRedisRepository;

    public void validateJoin(Long userId, Long restaurantId) {
        Restaurant restaurant = findRestaurantById(restaurantId);
        isPossibleWaiting(restaurant.getWaitingStatus());

        User user = findUserById(userId);

        String waitingKey = WaitingType.IN.getWaitingKey(restaurant.getId());

        boolean isExists = customerWaitingRedisRepository.zFindUserRank(waitingKey, user.getId())
                .isPresent();

        if (isExists) {
            throw new ConflictException(ErrorCode.ALREADY_REGISTERED_USER_IN_WAITING_QUEUE);
        }
    }

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

    @Transactional
    public void cancelWaitingQueue(Long userId, Long restaurantId, Long waitingId) {
        Restaurant restaurant = findRestaurantById(restaurantId);
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

        String key = waiting.getType().getWaitingKey(waiting.getRestaurant().getId());

        customerWaitingRedisRepository.zFindUserRank(key, userId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.WAITING_QUEUE_USER_NOT_FOUND));

        waiting.updateStatus(ReservationStatus.CANCELED);

        customerWaitingRedisRepository.zRemove(key, user.getId());

        // 유저에게 대기열 취소에 성공함을 알리는 알림 전송 추가
    }

    public WaitingQueueFindUserRankResponse findWaitingQueueUserRank(Long userId, Long restaurantId, Long waitingId) {
        Restaurant restaurant = findRestaurantById(restaurantId);
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
        Restaurant restaurant = findRestaurantById(restaurantId);
        isPossibleWaiting(restaurant.getWaitingStatus());

        String waitingKey = WaitingType.IN.getWaitingKey(restaurant.getId());
        Long waitingQueueSize = customerWaitingRedisRepository.zCard(waitingKey);

        return WaitingQueueFindSizeResponse.from(waitingQueueSize);
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

    private Restaurant findRestaurantById(Long restaurantId) {
        return customerRestaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.RESTAURANT_NOT_FOUND));
    }
}
