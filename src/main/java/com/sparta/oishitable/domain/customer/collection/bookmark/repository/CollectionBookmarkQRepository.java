package com.sparta.oishitable.domain.customer.collection.bookmark.repository;

import com.sparta.oishitable.domain.customer.bookmark.dto.response.BookmarkDetails;
import com.sparta.oishitable.domain.customer.collection.bookmark.entity.CollectionBookmark;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface CollectionBookmarkQRepository {

    Optional<CollectionBookmark> findByCollectionBookmarkId(Long collectionBookmarkId);

    boolean existsByCollectionIdAndBookmarkIds(Long collectionId, List<Long> bookmarkIds);

    Page<BookmarkDetails> findBookmarkDetailsPaginationByCollectionId(Long collectionId, Pageable pageable);
}
