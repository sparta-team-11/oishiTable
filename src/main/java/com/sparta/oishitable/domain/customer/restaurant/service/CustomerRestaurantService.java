package com.sparta.oishitable.domain.customer.restaurant.service;

import com.sparta.oishitable.domain.customer.restaurant.dto.response.RestaurantListResponse;
import com.sparta.oishitable.domain.customer.restaurant.dto.response.RestaurantResponse;
import com.sparta.oishitable.domain.owner.restaurant.entity.Restaurant;
import com.sparta.oishitable.domain.owner.restaurant.repository.RestaurantRepository;
import com.sparta.oishitable.global.exception.CustomRuntimeException;
import com.sparta.oishitable.global.exception.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CustomerRestaurantService {

    private final RestaurantRepository restaurantRepository;

    public Page<RestaurantListResponse> getRestaurants(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);

        Page<Restaurant> restaurants = restaurantRepository.findAll(pageable);

        return restaurants.map(RestaurantListResponse::from);
    }

    public RestaurantResponse getRestaurant(Long restaurantId) {
        Restaurant restaurant = findById(restaurantId);

        return RestaurantResponse.from(restaurant);
    }

    private Restaurant findById(Long restaurantId) {
        return restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new CustomRuntimeException(ErrorCode.RESTAURANT_NOT_FOUND));
    }
}
