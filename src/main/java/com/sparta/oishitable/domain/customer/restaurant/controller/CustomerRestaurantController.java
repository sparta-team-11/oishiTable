package com.sparta.oishitable.domain.customer.restaurant.controller;

import com.sparta.oishitable.domain.customer.restaurant.dto.response.NearbyRestaurantResponse;
import com.sparta.oishitable.domain.customer.restaurant.dto.response.RestaurantListResponse;
import com.sparta.oishitable.domain.customer.restaurant.dto.response.RestaurantResponse;
import com.sparta.oishitable.domain.customer.restaurant.service.CustomerRestaurantService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/customer/api/restaurants")
@RequiredArgsConstructor
public class CustomerRestaurantController {

    private final CustomerRestaurantService customerRestaurantService;

    @GetMapping
    public ResponseEntity<Page<RestaurantListResponse>> findRestaurants(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<RestaurantListResponse> restaurants = customerRestaurantService.findRestaurants(page,
                size);
        return ResponseEntity.ok(restaurants);
    }

    @GetMapping("/{restaurantId}")
    public ResponseEntity<RestaurantResponse> findRestaurant(@PathVariable Long restaurantId) {

        RestaurantResponse restaurant = customerRestaurantService.findRestaurant(restaurantId);

        return ResponseEntity.ok(restaurant);
    }

    @GetMapping("/nearby")
    public ResponseEntity<Page<NearbyRestaurantResponse>> findNearbyRestaurants(
            @RequestParam Double lat,
            @RequestParam Double lng,
            @RequestParam Double radius,
            @PageableDefault(size = 10, sort = "name") Pageable pageable) {

        Page<NearbyRestaurantResponse> restaurants = customerRestaurantService.findNearbyRestaurants(lat, lng, radius, pageable);

        return ResponseEntity.ok(restaurants);
    }
}
