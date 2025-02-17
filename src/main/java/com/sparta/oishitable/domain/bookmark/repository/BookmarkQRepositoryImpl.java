package com.sparta.oishitable.domain.bookmark.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.oishitable.domain.bookmark.entity.Bookmark;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

import static com.sparta.oishitable.domain.bookmark.entity.QBookmark.bookmark;

@RequiredArgsConstructor
public class BookmarkQRepositoryImpl implements BookmarkQRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public boolean existsByUserIdAndRestaurantId(Long userId, Long restaurantId) {
        Integer result = queryFactory
                .selectOne()
                .from(bookmark)
                .where(
                        bookmark.user.id.eq(userId),
                        bookmark.restaurant.id.eq(restaurantId)
                )
                .fetchFirst();

        return result != null;
    }

    @Override
    public Optional<Bookmark> findByUserIdAndRestaurantId(Long userId, Long restaurantId) {
        return Optional.ofNullable(queryFactory
                .selectFrom(bookmark)
                .where(
                        bookmark.user.id.eq(userId),
                        bookmark.restaurant.id.eq(restaurantId)
                )
                .fetchOne());
    }
}
