package com.sparta.oishitable.domain.bookmark.repository;

import com.sparta.oishitable.domain.bookmark.entity.Bookmark;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {
    boolean existsByUser_IdAndRestaurant_Id(Long userId, Long restaurantId);
}
