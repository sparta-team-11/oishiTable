package com.sparta.oishitable.domain.customer.restaurant.waiting.service;

import com.sparta.oishitable.domain.customer.restaurant.waiting.dto.request.WaitingJoinRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class CustomerRestaurantWaitingFacade {

    private final CustomerRestaurantWaitingService customerRestaurantWaitingService;

    @Transactional
    public void join(Long userId, Long restaurantId, WaitingJoinRequest request) {
        customerRestaurantWaitingService.validateJoin(userId, restaurantId, request);
        customerRestaurantWaitingService.joinWaitingQueue(userId, restaurantId, request);
        // 유저에게 대기열 등록에 성공함을 알리는 알림 전송 추가
    }
}
