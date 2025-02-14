package com.sparta.oishitable.domain.restaurant.controller;

import com.sparta.oishitable.domain.restaurant.dto.request.RestaurantCreateRequest;
import com.sparta.oishitable.domain.restaurant.dto.request.RestaurantProfileUpdateRequest;
import com.sparta.oishitable.domain.restaurant.service.OwnerRestaurantService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/restaurants")
@RequiredArgsConstructor
public class OwnerRestaurantController {

    private final OwnerRestaurantService ownerRestaurantService;

    @PostMapping
    public ResponseEntity<Void> createRestaurant(
            @RequestBody @Valid RestaurantCreateRequest restaurantCreateRequest
    ) {
        ownerRestaurantService.createRestaurant(restaurantCreateRequest);

        return ResponseEntity.created(null).build();
    }

    @PatchMapping("/{restaurantId}")
    public ResponseEntity<Void> updateRestaurant(
            @PathVariable Long restaurantId,
            @RequestBody @Valid RestaurantProfileUpdateRequest restaurantProfileUpdateRequest
    ) {
        ownerRestaurantService.updateRestaurantProfile(restaurantId, restaurantProfileUpdateRequest);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{restaurantId}")
    public ResponseEntity<Void> deleteRestaurant(
            @PathVariable Long restaurantId
    ) {
        ownerRestaurantService.deleteRestaurant(restaurantId);

        return ResponseEntity.noContent().build();
    }
}
