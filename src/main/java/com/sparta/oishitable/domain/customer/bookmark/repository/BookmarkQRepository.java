package com.sparta.oishitable.domain.customer.bookmark.repository;

import com.sparta.oishitable.domain.customer.bookmark.dto.response.BookmarkDetails;
import com.sparta.oishitable.domain.customer.bookmark.entity.Bookmark;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BookmarkQRepository {

    boolean existsByUserIdAndRestaurantId(Long userId, Long restaurantId);

    List<Bookmark> findAllByBookmarkIds(List<Long> bookmarkIds);

    Page<BookmarkDetails> findBookmarkDetailsPaginationByUserId(Long userId, Pageable pageable);
}
