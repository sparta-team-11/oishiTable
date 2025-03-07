package com.sparta.oishitable.domain.customer.restaurant.waiting.controller;

import com.sparta.oishitable.domain.customer.restaurant.waiting.dto.request.WaitingJoinRequest;
import com.sparta.oishitable.domain.customer.restaurant.waiting.dto.response.WaitingQueueFindSizeResponse;
import com.sparta.oishitable.domain.customer.restaurant.waiting.dto.response.WaitingQueueFindUserRankResponse;
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
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody WaitingJoinRequest request
    ) {
        customerRestaurantWaitingService.joinWaitingQueue(userDetails.getId(), restaurantId, request);

        // redirect uri: 유저 - 나의 예약 목록 조회 API  예정
        return ResponseEntity.created(null).build();
    }

    @GetMapping
    public ResponseEntity<WaitingQueueFindSizeResponse> findWaitingQueueSize(
            @PathVariable Long restaurantId
    ) {
        WaitingQueueFindSizeResponse waitingQueueSizeResponse
                = customerRestaurantWaitingService.findWaitingQueueSize(restaurantId);

        return ResponseEntity.ok(waitingQueueSizeResponse);
    }

    @GetMapping("/{waitingId}")
    public ResponseEntity<WaitingQueueFindUserRankResponse> findWaitingQueueUserRank(
            @PathVariable Long waitingId,
            @PathVariable Long restaurantId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        WaitingQueueFindUserRankResponse waitingQueueUserRankResponse
                = customerRestaurantWaitingService.findWaitingQueueUserRank(userDetails.getId(), restaurantId, waitingId);

        return ResponseEntity.ok(waitingQueueUserRankResponse);
    }

    @DeleteMapping("/{waitingId}")
    public ResponseEntity<Void> cancelWaiting(
            @PathVariable Long waitingId,
            @PathVariable Long restaurantId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        customerRestaurantWaitingService.cancelWaitingQueue(userDetails.getId(), restaurantId, waitingId);

        return ResponseEntity.noContent().build();
    }
}
