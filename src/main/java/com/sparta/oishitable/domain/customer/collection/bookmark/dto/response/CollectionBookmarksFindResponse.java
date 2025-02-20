package com.sparta.oishitable.domain.customer.collection.bookmark.dto.response;

import com.sparta.oishitable.domain.customer.bookmark.dto.response.BookmarkDetails;
import lombok.AccessLevel;
import lombok.Builder;
import org.springframework.data.domain.Page;

import java.util.List;

@Builder(access = AccessLevel.PRIVATE)
public record CollectionBookmarksFindResponse(
        List<BookmarkDetails> bookmarks,
        int curPage,
        int totalPages,
        long totalElements
) {

    public static CollectionBookmarksFindResponse from(Page<BookmarkDetails> bookmarkDetails) {
        return CollectionBookmarksFindResponse.builder()
                .bookmarks(bookmarkDetails.getContent())
                .curPage(bookmarkDetails.getNumber())
                .totalPages(bookmarkDetails.getTotalPages())
                .totalElements(bookmarkDetails.getTotalElements())
                .build();
    }
}
