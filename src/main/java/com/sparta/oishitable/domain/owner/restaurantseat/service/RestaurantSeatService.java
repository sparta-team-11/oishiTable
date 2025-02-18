package com.sparta.oishitable.domain.owner.restaurantseat.service;

import com.sparta.oishitable.domain.admin.seatType.service.SeatTypeService;
import com.sparta.oishitable.domain.owner.restaurant.entity.Restaurant;
import com.sparta.oishitable.domain.owner.restaurantseat.dto.request.RestaurantSeatCreateRequest;
import com.sparta.oishitable.domain.owner.restaurantseat.entity.RestaurantSeat;
import com.sparta.oishitable.domain.owner.restaurantseat.repository.RestaurantSeatRepository;
import com.sparta.oishitable.global.exception.NotFoundException;
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

    public RestaurantSeat findByRestaurantIdAndSeatTypeId(Long restaurantId, Long seatTypeId) {
        return restaurantSeatRepository.findByRestaurantIdAndSeatTypeId(restaurantId, seatTypeId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.RESTAURANT_SEAT_TYPE_NOT_FOUND));
    }
}
