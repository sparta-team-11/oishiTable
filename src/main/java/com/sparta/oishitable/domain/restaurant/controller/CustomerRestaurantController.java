package com.sparta.oishitable.domain.restaurant.controller;

import com.sparta.oishitable.domain.restaurant.dto.response.RestaurantListResponse;
import com.sparta.oishitable.domain.restaurant.dto.response.RestaurantResponse;
import com.sparta.oishitable.domain.restaurant.service.CustomerRestaurantService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/restaurants")
public class CustomerRestaurantController {

    private final CustomerRestaurantService customerRestaurantService;

    @GetMapping
    public ResponseEntity<Page<RestaurantListResponse>> readRestaurants(
        @RequestParam(defaultValue = "1") int page,
        @RequestParam(defaultValue = "10") int size
    ) {
        Page<RestaurantListResponse> restaurants = customerRestaurantService.getRestaurants(page,
            size);
        return ResponseEntity.ok(restaurants);
    }

    @GetMapping("/{restaurantId}")
    public ResponseEntity<RestaurantResponse> readRestaurant(@PathVariable Long restaurantId) {

        RestaurantResponse restaurant = customerRestaurantService.getRestaurant(restaurantId);

        return ResponseEntity.ok(restaurant);
    }
}
