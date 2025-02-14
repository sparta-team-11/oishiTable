package com.sparta.oishitable.domain.restaurantseat.service;

import com.sparta.oishitable.domain.restaurant.entity.Restaurant;
import com.sparta.oishitable.domain.restaurantseat.dto.request.RestaurantSeatCreateRequest;
import com.sparta.oishitable.domain.restaurantseat.entity.RestaurantSeat;
import com.sparta.oishitable.domain.restaurantseat.repository.RestaurantSeatRepository;
import com.sparta.oishitable.domain.seatType.entity.SeatType;
import com.sparta.oishitable.domain.seatType.service.SeatTypeService;
import com.sparta.oishitable.global.exception.CustomRuntimeException;
import com.sparta.oishitable.global.exception.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RestaurantSeatService {

    private final RestaurantSeatRepository restaurantSeatRepository;
    private final SeatTypeService seatTypeService;

    public void createAllRestaurantSeat(Restaurant restaurant, List<RestaurantSeatCreateRequest> restaurantSeatCreateRequestList) {
        List<RestaurantSeat> restaurantSeatList = restaurantSeatCreateRequestList.stream()
                .map(r -> r.toEntity(restaurant, seatTypeService.findSeatTypeById(r.seatTypeId())))
                .toList();

        // TODO: Refactoring?
        restaurantSeatRepository.saveAll(restaurantSeatList);
    }

    public void deleteAllRestaurantSeat(Restaurant restaurant) {
        restaurantSeatRepository.deleteByRestaurant(restaurant);
    }

    public RestaurantSeat findBySeatType(SeatType seatType) {
        return restaurantSeatRepository.findBySeatType(seatType)
                .orElseThrow(() -> new CustomRuntimeException(ErrorCode.RESTAURANT_SEAT_TYPE_NOT_FOUND));
    }
}
