package com.sparta.oishitable.domain.customer.restaurant.service;

import com.sparta.oishitable.domain.customer.restaurant.dto.response.RestaurantResponse;
import com.sparta.oishitable.domain.customer.restaurant.dto.response.RestaurantSimpleResponse;
import com.sparta.oishitable.domain.customer.restaurant.repository.CustomerRestaurantRepository;
import com.sparta.oishitable.domain.owner.restaurant.entity.Restaurant;
import com.sparta.oishitable.global.exception.CustomRuntimeException;
import com.sparta.oishitable.global.exception.error.ErrorCode;
import com.sparta.oishitable.global.util.GeometryUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerRestaurantService {

    private final CustomerRestaurantRepository restaurantRepository;

    private static final double EARTH_RADIUS = 6371;

    public Slice<RestaurantSimpleResponse> findRestaurants(
            Pageable pageable,
            String keyword,
            String address,
            Integer minPrice,
            Integer maxPrice,
            Long seatTypeId,
            Boolean isUseDistance,
            Double clientLat,
            Double clientLon,
            Integer distance
    ) {
        return restaurantRepository.findRestaurantsByFilters(
                pageable, keyword,
                address, minPrice,
                maxPrice, seatTypeId,
                isUseDistance, GeometryUtil.createPoint(clientLat, clientLon),
                distance
        );
    }

    public RestaurantResponse findRestaurant(Long restaurantId) {
        Restaurant restaurant = findById(restaurantId);

        return RestaurantResponse.from(restaurant);
    }

    private Restaurant findById(Long restaurantId) {
        return restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new CustomRuntimeException(ErrorCode.RESTAURANT_NOT_FOUND));
    }

    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {

        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.sin(dLon / 2) * Math.sin(dLon / 2) * Math.cos(lat1) * Math.cos(lat2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS * c;
    }
}
