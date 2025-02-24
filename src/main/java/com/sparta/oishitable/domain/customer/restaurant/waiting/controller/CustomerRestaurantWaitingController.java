package com.sparta.oishitable.domain.customer.restaurant.waiting.controller;

import com.sparta.oishitable.domain.customer.restaurant.waiting.dto.response.WaitingQueueSizeResponse;
import com.sparta.oishitable.domain.customer.restaurant.waiting.dto.response.WaitingQueueUserRankResponse;
import com.sparta.oishitable.domain.customer.restaurant.waiting.service.CustomerRestaurantWaitingService;
import com.sparta.oishitable.global.security.entity.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/customer/api/restaurants/{restaurantId}/waitings")
@RequiredArgsConstructor
public class CustomerRestaurantWaitingController {

    private final CustomerRestaurantWaitingService customerRestaurantWaitingService;

    @PostMapping
    public ResponseEntity<Void> joinWaitingQueue(
            @PathVariable Long restaurantId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        customerRestaurantWaitingService.joinWaitingQueue(userDetails.getId(), restaurantId);

        // redirect uri: 유저 - 나의 예약 목록 조회 API  예정
        return ResponseEntity.created(null).build();
    }

    @GetMapping
    public ResponseEntity<WaitingQueueSizeResponse> findWaitingQueueSize(
            @PathVariable Long restaurantId
    ) {
        WaitingQueueSizeResponse waitingQueueSizeResponse = customerRestaurantWaitingService.findWaitingQueueSize(restaurantId);

        return ResponseEntity.ok(waitingQueueSizeResponse);
    }

    @GetMapping("/rank")
    public ResponseEntity<WaitingQueueUserRankResponse> findWaitingQueueUserRank(
            @PathVariable Long restaurantId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        WaitingQueueUserRankResponse waitingQueueUserRankResponse = customerRestaurantWaitingService.findWaitingQueueUserRank(userDetails.getId(), restaurantId);

        return ResponseEntity.ok(waitingQueueUserRankResponse);
    }

    @DeleteMapping
    public ResponseEntity<Void> cancelWaiting(
            @PathVariable Long restaurantId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        customerRestaurantWaitingService.cancelWaitingQueue(userDetails.getId(), restaurantId);

        return ResponseEntity.noContent().build();
    }
}
