package com.sparta.oishitable.domain.restaurant.service;

import com.sparta.oishitable.domain.restaurant.dto.request.RestaurantCreateRequest;
import com.sparta.oishitable.domain.restaurant.entity.Restaurant;
import com.sparta.oishitable.domain.restaurant.repository.RestaurantRepository;
import com.sparta.oishitable.domain.restaurantseat.dto.request.RestaurantSeatCreateRequest;
import com.sparta.oishitable.domain.restaurantseat.service.RestaurantSeatService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OwnerRestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final RestaurantSeatService restaurantSeatService;

    @Transactional
    public void createRestaurant(RestaurantCreateRequest restaurantCreateRequest) {
        Restaurant restaurant = restaurantCreateRequest.toEntity();
        Restaurant savedRestaurant = restaurantRepository.save(restaurant);

        restaurantSeatService.createAllRestaurantSeat(savedRestaurant, restaurantCreateRequest.restaurantSeatCreateRequestList());
    }
}
