package com.sparta.oishitable.domain.customer.bookmark.repository;

import com.sparta.oishitable.domain.customer.bookmark.entity.Bookmark;

import java.util.Optional;

public interface BookmarkQRepository {

    boolean existsByUserIdAndRestaurantId(Long userId, Long restaurantId);

    Optional<Bookmark> findByUserIdAndRestaurantId(Long userId, Long restaurantId);
}
