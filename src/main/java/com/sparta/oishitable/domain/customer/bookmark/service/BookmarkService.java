package com.sparta.oishitable.domain.customer.bookmark.service;

import com.sparta.oishitable.domain.common.user.entity.User;
import com.sparta.oishitable.domain.common.user.repository.UserRepository;
import com.sparta.oishitable.domain.customer.bookmark.dto.request.BookmarkUpdateRequest;
import com.sparta.oishitable.domain.customer.bookmark.dto.response.BookmarkDetails;
import com.sparta.oishitable.domain.customer.bookmark.dto.response.BookmarksFindResponse;
import com.sparta.oishitable.domain.customer.bookmark.entity.Bookmark;
import com.sparta.oishitable.domain.customer.bookmark.repository.BookmarkRepository;
import com.sparta.oishitable.domain.owner.restaurant.entity.Restaurant;
import com.sparta.oishitable.domain.owner.restaurant.repository.RestaurantRepository;
import com.sparta.oishitable.global.exception.ConflictException;
import com.sparta.oishitable.global.exception.ForbiddenException;
import com.sparta.oishitable.global.exception.NotFoundException;
import com.sparta.oishitable.global.exception.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
        if (bookmarkRepository.existsByUserIdAndRestaurantId(userId, restaurantId)) {
            throw new ConflictException(ErrorCode.BOOKMARK_ALREADY_EXISTS_RESTAURANT);
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));

        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.RESTAURANT_NOT_FOUND));

        Bookmark bookmark = Bookmark.builder()
                .user(user)
                .restaurant(restaurant)
                .build();

        bookmarkRepository.save(bookmark);
    }

    public BookmarksFindResponse findBookmarks(Long userId, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);

        Page<BookmarkDetails> bookmarkDetails
                = bookmarkRepository.findBookmarkDetailsPaginationByUserId(userId, pageable);

        return BookmarksFindResponse.from(bookmarkDetails);
    }

    @Transactional
    public void updateBookmarkMemo(Long userId, Long bookmarkId, BookmarkUpdateRequest bookmarkUpdateRequest) {
        Bookmark bookmark = findById(bookmarkId);
        checkUserAuthority(userId, bookmark);

        bookmark.updateMemo(bookmarkUpdateRequest.updateMemo());
    }

    @Transactional
    public void deleteBookmark(Long userId, Long bookmarkId) {
        Bookmark bookmark = findById(bookmarkId);
        checkUserAuthority(userId, bookmark);

        bookmarkRepository.delete(bookmark);
    }

    private static void checkUserAuthority(Long userId, Bookmark bookmark) {
        if (!bookmark.getUser().getId().equals(userId)) {
            throw new ForbiddenException(ErrorCode.USER_UNAUTHORIZED);
        }
    }

    @Transactional
    public void deleteBookmarkByUserIdAndRestaurantId(Long userId, Long restaurantId) {
        Bookmark bookmark = findByUserIdAndRestaurantId(userId, restaurantId);

        bookmarkRepository.delete(bookmark);
    }

    private Bookmark findById(Long bookmarkId) {
        return bookmarkRepository.findById(bookmarkId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.BOOKMARK_NOT_FOUND));
    }

    private Bookmark findByUserIdAndRestaurantId(Long userId, Long restaurantId) {
        return bookmarkRepository.findByUserIdAndRestaurantId(userId, restaurantId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.BOOKMARK_NOT_FOUND));
    }
}
