package com.sparta.oishitable.domain.owner.restaurant.service;

import com.sparta.oishitable.domain.common.user.entity.User;
import com.sparta.oishitable.domain.common.user.repository.UserRepository;
import com.sparta.oishitable.domain.owner.restaurant.dto.request.RestaurantCreateRequest;
import com.sparta.oishitable.domain.owner.restaurant.dto.request.RestaurantProfileUpdateRequest;
import com.sparta.oishitable.domain.owner.restaurant.dto.response.RestaurantFindResponse;
import com.sparta.oishitable.domain.owner.restaurant.entity.Restaurant;
import com.sparta.oishitable.domain.owner.restaurant.repository.RestaurantRepository;
import com.sparta.oishitable.domain.owner.restaurantseat.service.RestaurantSeatService;
import com.sparta.oishitable.global.exception.CustomRuntimeException;
import com.sparta.oishitable.global.exception.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OwnerRestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final UserRepository userRepository;
    private final RestaurantSeatService restaurantSeatService;

    @Transactional
    public Long createRestaurant(RestaurantCreateRequest restaurantCreateRequest) {
        User owner = userRepository.findById(restaurantCreateRequest.userId())
                .orElseThrow(() -> new CustomRuntimeException(ErrorCode.USER_NOT_FOUND));

        Restaurant restaurant = restaurantCreateRequest.toEntity(owner);
        Restaurant savedRestaurant = restaurantRepository.save(restaurant);

        restaurantSeatService.createAllRestaurantSeat(
                savedRestaurant,
                restaurantCreateRequest.restaurantSeatCreateRequestList()
        );

        return savedRestaurant.getId();
    }

    public RestaurantFindResponse findRestaurant(Long restaurantId) {
        Restaurant restaurant = findById(restaurantId);

        return RestaurantFindResponse.from(restaurant);
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

    private Restaurant findById(Long restaurantId) {
        return restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new CustomRuntimeException(ErrorCode.RESTAURANT_NOT_FOUND));
    }
}
