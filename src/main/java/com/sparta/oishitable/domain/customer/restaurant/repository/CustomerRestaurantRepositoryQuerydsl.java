package com.sparta.oishitable.domain.customer.restaurant.repository;

import com.sparta.oishitable.domain.customer.restaurant.dto.response.RestaurantSimpleResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface CustomerRestaurantRepositoryQuerydsl {

    Slice<RestaurantSimpleResponse> findRestaurantsByFilters(
            Pageable pageable, String keyword,
            String address, Integer minPrice,
            Integer maxPrice, Long seatTypeId,
            Boolean isUseDistance, Double clientLat,
            Double clientLon, Integer distance
    );
}
