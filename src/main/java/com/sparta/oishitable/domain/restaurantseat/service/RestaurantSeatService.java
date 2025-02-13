package com.sparta.oishitable.domain.restaurantseat.service;

import com.sparta.oishitable.domain.restaurant.entity.Restaurant;
import com.sparta.oishitable.domain.restaurantseat.dto.request.RestaurantSeatCreateRequest;
import com.sparta.oishitable.domain.restaurantseat.entity.RestaurantSeat;
import com.sparta.oishitable.domain.restaurantseat.repository.RestaurantSeatRepository;
import com.sparta.oishitable.domain.seatType.entity.SeatType;
import com.sparta.oishitable.domain.seatType.service.SeatTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.IntStream;

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
}
