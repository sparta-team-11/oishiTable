package com.sparta.oishitable.domain.customer.collection.bookmark.controller;

import com.sparta.oishitable.domain.customer.collection.bookmark.dto.request.CollectionBookmarkCreateRequest;
import com.sparta.oishitable.domain.customer.collection.bookmark.dto.request.CollectionBookmarksCreateRequest;
import com.sparta.oishitable.domain.customer.collection.bookmark.service.CollectionBookmarkService;
import com.sparta.oishitable.global.security.entity.CustomUserDetails;
import com.sparta.oishitable.global.util.UriBuilderUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/customer/api/collections/{collectionId}/bookmarks")
@RequiredArgsConstructor
public class CollectionBookmarkController {

    private final CollectionBookmarkService collectionBookmarkService;

    @PostMapping
    public ResponseEntity<Void> createCollectionBookmark(
            @PathVariable Long collectionId,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody @Valid CollectionBookmarksCreateRequest collectionBookmarksCreateRequest
    ) {
        collectionBookmarkService
                .createCollectionBookmarks(userDetails.getId(), collectionId, collectionBookmarksCreateRequest);
        URI location = UriBuilderUtil.create("/api/customer/collections/{collectionId}", collectionId);

        return ResponseEntity.created(location).build();
    }

    @DeleteMapping("/{collectionBookmarkId}")
    public ResponseEntity<Void> deleteCollectionBookmarks(
            @PathVariable Long collectionId,
            @PathVariable Long collectionBookmarkId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        collectionBookmarkService.deleteCollectionBookmark(userDetails.getId(), collectionId, collectionBookmarkId);

        return ResponseEntity.noContent().build();
    }
}
