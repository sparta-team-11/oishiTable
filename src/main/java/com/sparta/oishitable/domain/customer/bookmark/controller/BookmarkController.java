package com.sparta.oishitable.domain.customer.bookmark.controller;

import com.sparta.oishitable.domain.customer.bookmark.dto.request.BookmarkCreateRequest;
import com.sparta.oishitable.domain.customer.bookmark.dto.request.BookmarkDeleteRequest;
import com.sparta.oishitable.domain.customer.bookmark.service.BookmarkService;
import com.sparta.oishitable.global.security.entity.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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

        return ResponseEntity.created(null).build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteBookmark(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody @Valid BookmarkDeleteRequest bookmarkDeleteReq
    ) {
        bookmarkService.deleteBookmark(userDetails.getId(), bookmarkDeleteReq.restaurantId());

        return ResponseEntity.noContent().build();
    }
}