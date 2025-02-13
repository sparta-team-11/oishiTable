package com.sparta.oishitable.domain.restaurant.controller;

import com.sparta.oishitable.domain.restaurant.dto.request.RestaurantCreateRequest;
import com.sparta.oishitable.domain.restaurant.service.OwnerRestaurantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/restaurants")
@RequiredArgsConstructor
public class OwnerRestaurantController {

    private final OwnerRestaurantService ownerRestaurantService;

    @PostMapping
    public ResponseEntity<Void> createRestaurant(
            @Validated @RequestBody RestaurantCreateRequest restaurantCreateRequest
    ) {
        ownerRestaurantService.createRestaurant(restaurantCreateRequest);

        return ResponseEntity.created(null).build();
    }
}
