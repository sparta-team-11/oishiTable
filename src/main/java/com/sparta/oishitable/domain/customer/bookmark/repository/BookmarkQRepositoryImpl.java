package com.sparta.oishitable.domain.customer.bookmark.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.oishitable.domain.customer.bookmark.dto.response.BookmarkDetails;
import com.sparta.oishitable.domain.customer.bookmark.dto.response.QBookmarkDetails;
import com.sparta.oishitable.domain.customer.bookmark.entity.Bookmark;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;
import java.util.Optional;

import static com.sparta.oishitable.domain.customer.bookmark.entity.QBookmark.bookmark;
import static com.sparta.oishitable.domain.owner.restaurant.entity.QRestaurant.restaurant;

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

    @Override
    public List<Bookmark> findAllByBookmarkIds(List<Long> bookmarkIds) {
        return queryFactory
                .selectFrom(bookmark)
                .where(bookmark.id.in(bookmarkIds))
                .fetch();
    }

    @Override
    public Page<BookmarkDetails> findBookmarkDetailsPaginationByUserId(Long userId, Pageable pageable) {
        List<BookmarkDetails> records = queryFactory
                .select(
                        new QBookmarkDetails(
                                bookmark.id,
                                bookmark.restaurant.id,
                                bookmark.memo,
                                restaurant.name,
                                restaurant.introduce,
                                restaurant.location
                        ))
                .from(bookmark)
                .innerJoin(bookmark.restaurant, restaurant)
                .where(bookmark.user.id.eq(userId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long count = queryFactory
                .select(bookmark.count())
                .from(bookmark)
                .where(bookmark.user.id.eq(userId))
                .fetchOne();

        return PageableExecutionUtils.getPage(records, pageable, () -> count);
    }
}
