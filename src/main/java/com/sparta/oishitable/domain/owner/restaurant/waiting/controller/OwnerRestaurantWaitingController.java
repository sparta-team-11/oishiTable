package com.sparta.oishitable.domain.owner.restaurant.waiting.controller;

import com.sparta.oishitable.domain.owner.restaurant.waiting.dto.request.WaitingQueueDeleteRequest;
import com.sparta.oishitable.domain.owner.restaurant.waiting.dto.request.WaitingQueueDeleteUserRequest;
import com.sparta.oishitable.domain.owner.restaurant.waiting.dto.response.WaitingQueueFindUsersResponse;
import com.sparta.oishitable.domain.owner.restaurant.waiting.service.OwnerRestaurantWaitingService;
import com.sparta.oishitable.global.security.entity.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/owner/api/restaurants/{restaurantId}/waitings")
@RequiredArgsConstructor
public class OwnerRestaurantWaitingController {

    private final OwnerRestaurantWaitingService ownerRestaurantWaitingService;

    @GetMapping
    public ResponseEntity<WaitingQueueFindUsersResponse> findWaitingQueue(
            @PathVariable Long restaurantId,
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "10") int size,
            @RequestParam(required = false, name = "type") String waitingType,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        WaitingQueueFindUsersResponse body = ownerRestaurantWaitingService
                .findWaitingUsers(userDetails.getId(), restaurantId, page, size, waitingType);

        return ResponseEntity.ok(body);
    }

    @PatchMapping
    public ResponseEntity<Void> updateWaitingStatus(
            @PathVariable Long restaurantId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        ownerRestaurantWaitingService.updateWaitingStatus(userDetails.getId(), restaurantId);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteWaitingUser(
            @PathVariable Long restaurantId,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody @Valid WaitingQueueDeleteUserRequest waitingQueueDeleteUserRequest
    ) {
        ownerRestaurantWaitingService
                .deleteUserFromWaitingQueue(userDetails.getId(), restaurantId, waitingQueueDeleteUserRequest);

        return ResponseEntity.noContent().build();
    }
}
