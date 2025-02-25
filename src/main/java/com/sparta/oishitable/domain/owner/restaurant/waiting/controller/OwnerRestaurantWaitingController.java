package com.sparta.oishitable.domain.owner.restaurant.waiting.controller;

import com.sparta.oishitable.domain.owner.restaurant.waiting.dto.request.WaitingQueueDeleteUserRequest;
import com.sparta.oishitable.domain.owner.restaurant.waiting.dto.response.WaitingQueueFindUsersResponse;
import com.sparta.oishitable.domain.owner.restaurant.waiting.service.OwnerRestaurantWaitingService;
import com.sparta.oishitable.global.security.entity.CustomUserDetails;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
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
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        WaitingQueueFindUsersResponse body
                = ownerRestaurantWaitingService.findWaitingUsers(userDetails.getId(), restaurantId, page, size);

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

    @DeleteMapping("/clear")
    public ResponseEntity<Void> clearWaitingQueue(
            @PathVariable Long restaurantId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        ownerRestaurantWaitingService.clearWaitingQueue(userDetails.getId(), restaurantId);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteWaitingCustomer(
            @PathVariable Long restaurantId,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody @Valid WaitingQueueDeleteUserRequest request
    ) {
        ownerRestaurantWaitingService.deleteUserFromWaitingQueue(userDetails.getId(), restaurantId, request.userId());

        return ResponseEntity.noContent().build();
    }
}
