package com.sparta.oishitable.domain.customer.restaurant.repository;

import com.sparta.oishitable.domain.owner.restaurant.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRestaurantRepository extends JpaRepository<Restaurant, Long>, CustomerRestaurantRepositoryQuerydsl {
}
