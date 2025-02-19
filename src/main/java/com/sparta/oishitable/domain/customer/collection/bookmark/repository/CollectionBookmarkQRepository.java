package com.sparta.oishitable.domain.customer.collection.bookmark.repository;

import com.sparta.oishitable.domain.customer.collection.bookmark.entity.CollectionBookmark;

import java.util.List;
import java.util.Optional;

public interface CollectionBookmarkQRepository {

    Optional<CollectionBookmark> findByCollectionBookmarkId(Long collectionBookmarkId);

    boolean existsByCollectionIdAndBookmarkIds(Long collectionId, List<Long> bookmarkIds);
}
