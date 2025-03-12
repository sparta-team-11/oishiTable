package com.sparta.oishitable.domain.owner.restaurant.waiting.service;

import com.sparta.oishitable.domain.common.auth.service.AuthService;
import com.sparta.oishitable.domain.customer.reservation.entity.ReservationStatus;
import com.sparta.oishitable.domain.owner.restaurant.entity.Restaurant;
import com.sparta.oishitable.domain.owner.restaurant.service.OwnerRestaurantService;
import com.sparta.oishitable.domain.owner.restaurant.waiting.dto.request.WaitingQueueDeleteUserRequest;
import com.sparta.oishitable.domain.owner.restaurant.waiting.dto.response.WaitingDetails;
import com.sparta.oishitable.domain.owner.restaurant.waiting.dto.response.WaitingQueueFindUsersResponse;
import com.sparta.oishitable.domain.owner.restaurant.waiting.entity.Waiting;
import com.sparta.oishitable.domain.owner.restaurant.waiting.entity.WaitingType;
import com.sparta.oishitable.domain.owner.restaurant.waiting.repository.OwnerRestaurantWaitingRedisRepository;
import com.sparta.oishitable.domain.owner.restaurant.waiting.repository.OwnerRestaurantWaitingRepository;
import com.sparta.oishitable.global.exception.ConflictException;
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

    private final AuthService authService;
    private final OwnerRestaurantService ownerRestaurantService;

    private final OwnerRestaurantWaitingRepository ownerRestaurantWaitingRepository;
    private final OwnerRestaurantWaitingRedisRepository ownerRestaurantWaitingRedisRepository;

    public WaitingQueueFindUsersResponse findWaitingUsers(
            Long ownerId,
            Long restaurantId,
            int page,
            int size,
            String waitingType
    ) {
        Restaurant restaurant = ownerRestaurantService.findById(restaurantId);
        authService.checkUserAuthority(restaurant.getOwner().getId(), ownerId);

        String key = WaitingType.of(waitingType).getWaitingKey(restaurant.getId());

        Long totalElements = ownerRestaurantWaitingRedisRepository.findQueueSize(key);

        int totalPages = (int) Math.ceil((double) totalElements / size) - 1;

        if (totalPages < 0) {
            totalPages = 0;
        }

        List<WaitingDetails> waitingUserDetails = Collections.emptyList();

        if (totalElements == 0) {
            return WaitingQueueFindUsersResponse.from(waitingUserDetails, page, totalPages, totalElements);
        }

        List<Long> userIds = ownerRestaurantWaitingRedisRepository.findWaitingUsers(key, page, size);

        if (!userIds.isEmpty()) {
            waitingUserDetails = ownerRestaurantWaitingRepository.findWaitingDetails(userIds);
        }

        return WaitingQueueFindUsersResponse.from(waitingUserDetails, page, totalPages, totalElements);
    }

    @Transactional
    public void updateWaitingStatus(Long ownerId, Long restaurantId) {
        Restaurant restaurant = ownerRestaurantService.findById(restaurantId);
        authService.checkUserAuthority(restaurant.getOwner().getId(), ownerId);

        restaurant.switchWaitingStatus();
    }

    @Transactional
    public void deleteUserFromWaitingQueue(Long ownerId, Long restaurantId, WaitingQueueDeleteUserRequest request) {
        Restaurant restaurant = ownerRestaurantService.findById(restaurantId);
        authService.checkUserAuthority(restaurant.getOwner().getId(), ownerId);

        Waiting waiting = findWaitingById(request.waitingId());

        if (waiting.getStatus().equals(ReservationStatus.COMPLETED)) {
            throw new ConflictException(ErrorCode.ALREADY_COMPLETED_WAITING_EXCEPTION);
        }

        if (waiting.getStatus().equals(ReservationStatus.CANCELED)) {
            throw new ConflictException(ErrorCode.ALREADY_CANCELED_WAITING_EXCEPTION);
        }

        WaitingType waitingType = waiting.getType();
        String key = waitingType.getWaitingKey(restaurant.getId());

        Long deleteCount = ownerRestaurantWaitingRedisRepository.deleteWaitingUser(key, waiting.getUser().getId());

        if (deleteCount == 0) {
            throw new NotFoundException(ErrorCode.WAITING_QUEUE_USER_NOT_FOUND);
        }

        waiting.updateStatus(ReservationStatus.CANCELED);

        // 삭제된 유저에게 취소 됨을 알리는 알림 전송 추가
    }

    private Waiting findWaitingById(Long waitingId) {
        return ownerRestaurantWaitingRepository.findById(waitingId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.WAITING_NOT_FOUND));
    }
}
