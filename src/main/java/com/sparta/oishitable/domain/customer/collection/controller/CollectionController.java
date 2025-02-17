package com.sparta.oishitable.domain.customer.collection.controller;

import com.sparta.oishitable.domain.customer.collection.dto.request.CollectionCreateRequest;
import com.sparta.oishitable.domain.customer.collection.dto.request.CollectionUpdateRequest;
import com.sparta.oishitable.domain.customer.collection.dto.response.CollectionDetailResponse;
import com.sparta.oishitable.domain.customer.collection.dto.response.CollectionInfoResponse;
import com.sparta.oishitable.domain.customer.collection.service.CollectionService;
import com.sparta.oishitable.global.security.entity.CustomUserDetails;
import com.sparta.oishitable.global.util.UriBuilderUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/collections")
@RequiredArgsConstructor
public class CollectionController {

    private final CollectionService collectionService;

    @PostMapping
    public ResponseEntity<Void> createCollection(
            @AuthenticationPrincipal CustomUserDetails user,
            @RequestBody @Valid CollectionCreateRequest collectionCreateRequest
    ) {
        Long collectionId = collectionService.createCollection(user.getId(), collectionCreateRequest);
        URI redirectUri = UriBuilderUtil.create("/api/collections/{collectionId}", collectionId);

        return ResponseEntity.created(redirectUri).build();
    }

    @GetMapping("/{collectionId}")
    public ResponseEntity<CollectionDetailResponse> findCollection(
            @PathVariable Long collectionId
    ) {
        CollectionDetailResponse collection = collectionService.findCollection(collectionId);

        return ResponseEntity.ok(collection);
    }

    @GetMapping
    public ResponseEntity<List<CollectionInfoResponse>> findCollections(
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        List<CollectionInfoResponse> collections = collectionService.findCollections(user.getId());

        return ResponseEntity.ok(collections);
    }

    @PatchMapping("/{collectionId}")
    public ResponseEntity<Void> updateCollection(
            @PathVariable Long collectionId,
            @AuthenticationPrincipal CustomUserDetails user,
            @RequestBody @Valid CollectionUpdateRequest collectionUpdateRequest
    ) {
        collectionService.updateCollection(user.getId(), collectionId, collectionUpdateRequest);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{collectionId}")
    public ResponseEntity<Void> deleteCollection(
            @PathVariable Long collectionId,
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        collectionService.deleteCollection(user.getId(), collectionId);

        return ResponseEntity.noContent().build();
    }
}
