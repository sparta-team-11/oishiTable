package com.sparta.oishitable.domain.customer.collection.bookmark.repository;

import com.sparta.oishitable.domain.customer.collection.bookmark.entity.CollectionBookmark;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CollectionBookmarkRepository extends JpaRepository<CollectionBookmark, Long>, CollectionBookmarkQRepository {
}
