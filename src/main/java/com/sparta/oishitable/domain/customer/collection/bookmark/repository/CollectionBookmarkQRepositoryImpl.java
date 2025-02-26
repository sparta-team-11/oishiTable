package com.sparta.oishitable.domain.customer.collection.bookmark.repository;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.oishitable.domain.customer.collection.bookmark.dto.response.CollectionBookmarkDetails;
import com.sparta.oishitable.domain.customer.collection.bookmark.dto.response.QCollectionBookmarkDetails;
import com.sparta.oishitable.domain.customer.collection.bookmark.entity.CollectionBookmark;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;
import java.util.Optional;

import static com.sparta.oishitable.domain.customer.bookmark.entity.QBookmark.bookmark;
import static com.sparta.oishitable.domain.customer.collection.bookmark.entity.QCollectionBookmark.collectionBookmark;
import static com.sparta.oishitable.domain.owner.restaurant.entity.QRestaurant.restaurant;

@RequiredArgsConstructor
public class CollectionBookmarkQRepositoryImpl implements CollectionBookmarkQRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<CollectionBookmark> findByCollectionBookmarkId(Long collectionBookmarkId) {
        return Optional.ofNullable(queryFactory
                .selectFrom(collectionBookmark)
                .where(collectionBookmark.id.eq(collectionBookmarkId))
                .innerJoin(collectionBookmark.bookmark, bookmark).fetchJoin()
                .fetchOne());
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

    @Override
    public Page<CollectionBookmarkDetails> findBookmarkDetailsByCollectionId(Long collectionId, Pageable pageable) {
        List<CollectionBookmarkDetails> records = queryFactory
                .select(new QCollectionBookmarkDetails(
                        bookmark.id,
                        bookmark.restaurant.id,
                        bookmark.memo,
                        restaurant.name,
                        restaurant.introduce,
                        restaurant.address,
                        restaurant.longitude,
                        restaurant.latitude
                ))
                .from(collectionBookmark)
                .innerJoin(collectionBookmark.bookmark, bookmark)
                .innerJoin(bookmark.restaurant, restaurant)
                .where(collectionBookmark.collection.id.eq(collectionId))
                .orderBy(collectionBookmark.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> count = queryFactory
                .select(collectionBookmark.count())
                .from(collectionBookmark)
                .where(collectionBookmark.collection.id.eq(collectionId));

        return PageableExecutionUtils.getPage(records, pageable, count::fetchOne);
    }
}
