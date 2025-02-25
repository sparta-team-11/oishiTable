package com.sparta.oishitable.domain.customer.restaurant.controller;

import com.sparta.oishitable.domain.customer.restaurant.dto.response.NearbyRestaurantResponse;
import com.sparta.oishitable.domain.customer.restaurant.dto.response.RestaurantResponse;
import com.sparta.oishitable.domain.customer.restaurant.dto.response.RestaurantSimpleResponse;
import com.sparta.oishitable.domain.customer.restaurant.service.CustomerRestaurantService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/customer/api/restaurants")
@RequiredArgsConstructor
public class CustomerRestaurantController {

    private final CustomerRestaurantService customerRestaurantService;

    @GetMapping
    public ResponseEntity<Slice<RestaurantSimpleResponse>> findRestaurants(
            @PageableDefault Pageable pageable,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String address,
            @RequestParam(required = false) Integer minPrice,
            @RequestParam(required = false) Integer maxPrice,
            @RequestParam(required = false) String seatTypeName
    ) {
        Slice<RestaurantSimpleResponse> response = customerRestaurantService.findRestaurants(
                pageable,
                keyword,
                address,
                minPrice,
                maxPrice,
                seatTypeName
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{restaurantId}")
    public ResponseEntity<RestaurantResponse> findRestaurant(
            @PathVariable Long restaurantId
    ) {
        RestaurantResponse restaurant = customerRestaurantService.findRestaurant(restaurantId);

        return ResponseEntity.ok(restaurant);
    }

    @GetMapping("/nearby")
    public ResponseEntity<Page<NearbyRestaurantResponse>> findNearbyRestaurants(
            @RequestParam Double lat,
            @RequestParam Double lng,
            @RequestParam Double radius,
            @PageableDefault(sort = "name") Pageable pageable
    ) {
        Page<NearbyRestaurantResponse> restaurants = customerRestaurantService.findNearbyRestaurants(lat, lng, radius, pageable);

        return ResponseEntity.ok(restaurants);
    }
}
