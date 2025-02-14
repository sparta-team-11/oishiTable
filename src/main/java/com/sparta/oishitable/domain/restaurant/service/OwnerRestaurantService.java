package com.sparta.oishitable.domain.restaurant.service;

import com.sparta.oishitable.domain.restaurant.dto.request.RestaurantCreateRequest;
import com.sparta.oishitable.domain.restaurant.dto.request.RestaurantProfileUpdateRequest;
import com.sparta.oishitable.domain.restaurant.entity.Restaurant;
import com.sparta.oishitable.domain.restaurant.repository.RestaurantRepository;
import com.sparta.oishitable.domain.restaurantseat.dto.request.RestaurantSeatCreateRequest;
import com.sparta.oishitable.domain.restaurantseat.service.RestaurantSeatService;
import com.sparta.oishitable.global.exception.CustomRuntimeException;
import com.sparta.oishitable.global.exception.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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

        restaurantSeatService.createAllRestaurantSeat(
                savedRestaurant,
                restaurantCreateRequest.restaurantSeatCreateRequestList()
        );
    }

    @Transactional
    public void updateRestaurantProfile(Long restaurantId, RestaurantProfileUpdateRequest restaurantProfileUpdateRequest) {
        Restaurant restaurant = findById(restaurantId);

        restaurant.updateProfile(
                restaurantProfileUpdateRequest.name(),
                restaurantProfileUpdateRequest.introduce(),
                restaurantProfileUpdateRequest.deposit()
        );
    }

    @Transactional
    public void deleteRestaurant(Long restaurantId) {
        Restaurant restaurant = findById(restaurantId);

        restaurantSeatService.deleteAllRestaurantSeat(restaurant);
        restaurantRepository.delete(restaurant);
    }

    public Restaurant findById(Long restaurantId) {
        return restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new CustomRuntimeException(ErrorCode.RESTAURANT_NOT_FOUND));
    }
}
