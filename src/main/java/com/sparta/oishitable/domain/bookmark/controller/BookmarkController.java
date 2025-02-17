package com.sparta.oishitable.domain.bookmark.controller;

import com.sparta.oishitable.domain.bookmark.dto.request.BookmarkCreateRequest;
import com.sparta.oishitable.domain.bookmark.dto.request.BookmarkDeleteRequest;
import com.sparta.oishitable.domain.bookmark.service.BookmarkService;
import com.sparta.oishitable.global.security.entity.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bookmarks")
@RequiredArgsConstructor
public class BookmarkController {

    private final BookmarkService bookmarkService;

    @PostMapping
    public ResponseEntity<Void> createBookmark(
            @AuthenticationPrincipal CustomUserDetails user,
            @Valid @RequestBody BookmarkCreateRequest bookmarkCreateReq
    ) {
        Long userId = Long.valueOf(user.getId());
        bookmarkService.createBookmark(userId, bookmarkCreateReq.restaurantId());

        return ResponseEntity.created(null).build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteBookmark(
            @AuthenticationPrincipal CustomUserDetails user,
            @Valid @RequestBody BookmarkDeleteRequest bookmarkDeleteReq
    ) {
        Long userId = Long.valueOf(user.getId());
        bookmarkService.deleteBookmark(userId, bookmarkDeleteReq.restaurantId());

        return ResponseEntity.noContent().build();
    }
}