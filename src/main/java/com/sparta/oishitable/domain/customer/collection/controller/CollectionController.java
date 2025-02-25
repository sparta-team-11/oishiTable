package com.sparta.oishitable.domain.customer.collection.controller;

import com.sparta.oishitable.domain.customer.collection.dto.request.CollectionCreateRequest;
import com.sparta.oishitable.domain.customer.collection.dto.request.CollectionUpdateRequest;
import com.sparta.oishitable.domain.customer.collection.dto.response.CollectionDetailResponse;
import com.sparta.oishitable.domain.customer.collection.dto.response.CollectionInfosResponse;
import com.sparta.oishitable.domain.customer.collection.service.CollectionService;
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
@RequestMapping("/customer/api/collections")
@RequiredArgsConstructor
public class CollectionController {

    private final CollectionService collectionService;

    @PostMapping
    public ResponseEntity<Void> createCollection(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody @Valid CollectionCreateRequest collectionCreateRequest
    ) {
        Long collectionId = collectionService.createCollection(userDetails.getId(), collectionCreateRequest);
        URI location = UriBuilderUtil.create("/api/collections/{collectionId}", collectionId);

        return ResponseEntity.created(location).build();
    }

    @GetMapping("/{collectionId}")
    public ResponseEntity<CollectionDetailResponse> findCollection(
            @PathVariable Long collectionId
    ) {
        CollectionDetailResponse body = collectionService.findCollection(collectionId);

        return ResponseEntity.ok(body);
    }

    @GetMapping
    public ResponseEntity<CollectionInfosResponse> findCollections(
            @PageableDefault Pageable pageable,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        CollectionInfosResponse body = collectionService.findCollections(userDetails.getId(), pageable);

        return ResponseEntity.ok(body);
    }

    @PatchMapping("/{collectionId}")
    public ResponseEntity<Void> updateCollection(
            @PathVariable Long collectionId,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody @Valid CollectionUpdateRequest collectionUpdateRequest
    ) {
        collectionService.updateCollection(userDetails.getId(), collectionId, collectionUpdateRequest);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{collectionId}")
    public ResponseEntity<Void> deleteCollection(
            @PathVariable Long collectionId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        collectionService.deleteCollection(userDetails.getId(), collectionId);

        return ResponseEntity.noContent().build();
    }
}
