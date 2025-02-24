package com.sparta.oishitable.domain.customer.restaurant.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.oishitable.domain.customer.restaurant.dto.response.QRestaurantSimpleResponse;
import com.sparta.oishitable.domain.customer.restaurant.dto.response.RestaurantSimpleResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

import java.util.List;

import static com.sparta.oishitable.domain.admin.seatType.entity.QSeatType.seatType;
import static com.sparta.oishitable.domain.owner.restaurant.entity.QRestaurant.restaurant;
import static com.sparta.oishitable.domain.owner.restaurant.menus.entity.QMenu.menu;
import static com.sparta.oishitable.domain.owner.restaurantseat.entity.QRestaurantSeat.restaurantSeat;

@RequiredArgsConstructor
public class CustomerRestaurantRepositoryQuerydslImpl implements CustomerRestaurantRepositoryQuerydsl {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Slice<RestaurantSimpleResponse> findRestaurantsByFilters(
            Pageable pageable,
            String keyword,
            String address,
            Integer minPrice,
            Integer maxPrice,
            String seatTypeName
    ) {
        BooleanBuilder builder = new BooleanBuilder();

        if (keyword != null) {
            builder.and(restaurant.name.containsIgnoreCase(keyword).or(menu.name.containsIgnoreCase(keyword)));
        }

        if (address != null) {
            builder.and(restaurant.address.contains(address));
        }

        if (minPrice != null && maxPrice != null) {
            builder.and(restaurant.minPrice.loe(maxPrice)).and(restaurant.maxPrice.goe(minPrice));
        }

        if (seatTypeName != null) {
            builder.and(seatType.name.contains(seatTypeName));
        }

        List<RestaurantSimpleResponse> records = jpaQueryFactory.select(
                        new QRestaurantSimpleResponse(
                                restaurant.id,
                                restaurant.name,
                                restaurant.address
                        )
                )
                .from(restaurant)
                .leftJoin(restaurant.menus, menu)
                .innerJoin(restaurantSeat)
                .on(restaurantSeat.restaurant.eq(restaurant))
                .innerJoin(seatType)
                .on(restaurantSeat.seatType.eq(seatType))
                .where(builder)
                .groupBy(restaurant.id)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        boolean hasNext = records.size() > pageable.getPageSize();

        if (hasNext) {
            records.remove(records.size() - 1);
        }

        return new SliceImpl<>(records, pageable, hasNext);
    }
}
