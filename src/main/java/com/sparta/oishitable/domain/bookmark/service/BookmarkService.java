package com.sparta.oishitable.domain.bookmark.service;

import com.sparta.oishitable.domain.bookmark.entity.Bookmark;
import com.sparta.oishitable.domain.bookmark.repository.BookmarkRepository;
import com.sparta.oishitable.domain.restaurant.entity.Restaurant;
import com.sparta.oishitable.domain.restaurant.repository.RestaurantRepository;
import com.sparta.oishitable.domain.user.entity.User;
import com.sparta.oishitable.domain.user.repository.UserRepository;
import com.sparta.oishitable.global.exception.CustomRuntimeException;
import com.sparta.oishitable.global.exception.NotFoundException;
import com.sparta.oishitable.global.exception.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BookmarkService {

    private final BookmarkRepository bookmarkRepository;
    private final RestaurantRepository restaurantRepository;
    private final UserRepository userRepository;

    @Transactional
    public void createBookmark(Long userId, Long restaurantId) {
        if (bookmarkRepository.existsByUser_IdAndRestaurant_Id(userId, restaurantId))  {
            throw new CustomRuntimeException(ErrorCode.BOOKMARK_ALREADY_EXISTS_RESTAURANT);
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND.getMessage()));

        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.RESTAURANT_NOT_FOUND.getMessage()));

        Bookmark bookmark = Bookmark.builder()
                .user(user)
                .restaurant(restaurant)
                .build();

        bookmarkRepository.save(bookmark);
    }

    @Transactional
    public void deleteBookmark(Long userId, Long bookmarkId) {
        Bookmark bookmark = findById(bookmarkId);

        if (!bookmark.getUser().getId().equals(userId)) {
            throw new CustomRuntimeException(ErrorCode.USER_UNAUTHORIZED);
        }

        bookmarkRepository.delete(bookmark);
    }

    private Bookmark findById(Long bookmarkId) {
        return bookmarkRepository.findById(bookmarkId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.BOOKMARK_NOT_FOUND.getMessage()));
    }
}
