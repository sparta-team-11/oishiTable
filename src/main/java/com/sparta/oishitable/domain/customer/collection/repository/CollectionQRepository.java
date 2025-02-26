package com.sparta.oishitable.domain.customer.collection.repository;

import com.sparta.oishitable.domain.customer.collection.dto.response.CollectionDetailResponse;
import com.sparta.oishitable.domain.customer.collection.dto.response.CollectionInfoResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface CollectionQRepository {

    Optional<CollectionDetailResponse> findCollectionDetail(Long collectionId);

    Page<CollectionInfoResponse> findAllByCollectionOwnerId(Long userId, Pageable pageable);

    Page<CollectionInfoResponse> findAllByPublicCollections(Long userId, Pageable pageable);
}
