package com.sparta.oishitable.domain.customer.bookmark.repository;

import com.sparta.oishitable.domain.customer.bookmark.entity.Bookmark;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long>, BookmarkQRepository {
}
