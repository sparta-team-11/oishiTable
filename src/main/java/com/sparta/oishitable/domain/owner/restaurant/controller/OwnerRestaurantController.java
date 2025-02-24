package com.sparta.oishitable.domain.owner.restaurant.controller;

import com.sparta.oishitable.domain.owner.restaurant.dto.request.RestaurantCreateRequest;
import com.sparta.oishitable.domain.owner.restaurant.dto.request.RestaurantProfileUpdateRequest;
import com.sparta.oishitable.domain.owner.restaurant.dto.response.RestaurantFindResponse;
import com.sparta.oishitable.domain.owner.restaurant.service.OwnerRestaurantService;
import com.sparta.oishitable.global.util.UriBuilderUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/owner/api/restaurants")
@RequiredArgsConstructor
public class OwnerRestaurantController {

    private final OwnerRestaurantService ownerRestaurantService;

    @PostMapping
    public ResponseEntity<Void> createRestaurant(
            @RequestBody @Valid RestaurantCreateRequest restaurantCreateRequest
    ) {
        Long restaurantId = ownerRestaurantService.createRestaurant(restaurantCreateRequest);
        URI location = UriBuilderUtil.create("/owner/api/restaurants/{restaurantId}", restaurantId);

        return ResponseEntity.created(location).build();
    }

    @GetMapping("/{restaurantId}")
    public ResponseEntity<RestaurantFindResponse> findRestaurant(
            @PathVariable Long restaurantId
    ) {
        RestaurantFindResponse restaurant = ownerRestaurantService.findRestaurant(restaurantId);

        return ResponseEntity.ok(restaurant);
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
