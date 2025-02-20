package com.sparta.oishitable.domain.customer.bookmark.controller;

import com.sparta.oishitable.domain.customer.bookmark.dto.request.BookmarkCreateRequest;
import com.sparta.oishitable.domain.customer.bookmark.dto.request.BookmarkDeleteRequest;
import com.sparta.oishitable.domain.customer.bookmark.dto.request.BookmarkUpdateRequest;
import com.sparta.oishitable.domain.customer.bookmark.dto.response.BookmarksFindResponse;
import com.sparta.oishitable.domain.customer.bookmark.service.BookmarkService;
import com.sparta.oishitable.global.security.entity.CustomUserDetails;
import com.sparta.oishitable.global.util.UriBuilderUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/customer/api/bookmarks")
@RequiredArgsConstructor
public class BookmarkController {

    private final BookmarkService bookmarkService;

    @PostMapping
    public ResponseEntity<Void> createBookmark(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody @Valid BookmarkCreateRequest bookmarkCreateReq
    ) {
        bookmarkService.createBookmark(userDetails.getId(), bookmarkCreateReq.restaurantId());

        String path = "/customer/api/restaurants/{restaurantsId}";
        URI location = UriBuilderUtil.create(path, bookmarkCreateReq.restaurantId());

        return ResponseEntity.created(location).build();
    }

    @GetMapping
    public ResponseEntity<BookmarksFindResponse> findBookmarks(
            @PageableDefault Pageable pageable,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        BookmarksFindResponse body = bookmarkService.findBookmarks(userDetails.getId(), pageable);

        return ResponseEntity.ok(body);
    }

    @PatchMapping("/{bookmarkId}")
    public ResponseEntity<Void> updateBookmark(
            @PathVariable Long bookmarkId,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody BookmarkUpdateRequest bookmarkUpdateRequest
    ) {
        bookmarkService.updateBookmarkMemo(userDetails.getId(), bookmarkId, bookmarkUpdateRequest);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{bookmarkId}")
    public ResponseEntity<Void> deleteBookmark(
            @PathVariable Long bookmarkId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        bookmarkService.deleteBookmark(userDetails.getId(), bookmarkId);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteBookmarkByUserIdAndRestaurantId(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody @Valid BookmarkDeleteRequest bookmarkDeleteReq
    ) {
        bookmarkService.deleteBookmarkByUserIdAndRestaurantId(userDetails.getId(), bookmarkDeleteReq.restaurantId());

        return ResponseEntity.noContent().build();
    }
}