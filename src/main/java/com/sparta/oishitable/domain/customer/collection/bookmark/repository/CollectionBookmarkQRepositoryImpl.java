package com.sparta.oishitable.domain.customer.collection.bookmark.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.oishitable.domain.customer.collection.bookmark.entity.CollectionBookmark;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

import static com.sparta.oishitable.domain.customer.bookmark.entity.QBookmark.bookmark;
import static com.sparta.oishitable.domain.customer.collection.bookmark.entity.QCollectionBookmark.collectionBookmark;

@RequiredArgsConstructor
public class CollectionBookmarkQRepositoryImpl implements CollectionBookmarkQRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<CollectionBookmark> findByCollectionBookmarkId(Long collectionBookmarkId) {
        return Optional.ofNullable(queryFactory
                .selectFrom(collectionBookmark)
                .where(collectionBookmark.id.eq(collectionBookmarkId))
                .innerJoin(collectionBookmark.bookmark, bookmark)
                .fetchOne());
    }

    @Override
    public boolean existsByCollectionIdAndBookmarkId(Long collectionId, Long bookmarkId) {
        Integer result = queryFactory
                .selectOne()
                .from(collectionBookmark)
                .where(
                        collectionBookmark.collection.id.eq(collectionId),
                        collectionBookmark.bookmark.id.eq(bookmarkId)
                )
                .fetchFirst();

        return result != null;
    }

    @Override
    public boolean existsByCollectionIdAndBookmarkIds(Long collectionId, List<Long> bookmarkIds) {
        Integer result = queryFactory
                .selectOne()
                .from(collectionBookmark)
                .where(
                        collectionBookmark.collection.id.eq(collectionId),
                        collectionBookmark.bookmark.id.in(bookmarkIds)
                )
                .fetchFirst();

        return result != null;
    }
}
