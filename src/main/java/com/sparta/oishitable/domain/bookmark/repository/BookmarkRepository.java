package com.sparta.oishitable.domain.bookmark.repository;

import com.sparta.oishitable.domain.bookmark.entity.Bookmark;
import com.sparta.oishitable.domain.restaurant.entity.Restaurant;
import com.sparta.oishitable.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookmarkRepository extends JpaRepository<Bookmark, Integer> {
    boolean existsByUserAndRestaurant(User user, Restaurant restaurant);

    boolean existsByUser_IdAndRestaurant_Id(Long userId, Long restaurantId);
}
