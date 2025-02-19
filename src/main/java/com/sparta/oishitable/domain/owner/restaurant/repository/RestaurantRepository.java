package com.sparta.oishitable.domain.owner.restaurant.repository;

import com.sparta.oishitable.domain.owner.restaurant.entity.Restaurant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
    @Query("SELECT r FROM Restaurant r " +
            "WHERE (:lat IS NULL OR :lng IS NULL OR " +
            "(6371 * acos(cos(radians(:lat)) * cos(radians(r.latitude)) * cos(radians(r.longitude) - " +
            "radians(:lng)) + sin(radians(:lat)) * sin(radians(r.latitude)))) <= :radius) ")
    Page<Restaurant> findNearbyRestaurants(
            @Param("lat") Double lat,
            @Param("lng") Double lng,
            @Param("radius") Double radius,
            Pageable pageable);
}