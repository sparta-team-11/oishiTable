package com.sparta.oishitable.domain.customer.restaurant.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberTemplate;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.oishitable.domain.customer.restaurant.dto.response.QRestaurantSimpleResponse;
import com.sparta.oishitable.domain.customer.restaurant.dto.response.RestaurantSimpleResponse;
import com.sparta.oishitable.domain.customer.restaurant.model.RestaurantSearchDistance;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Point;
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
            Long seatTypeId,
            Boolean isUseDistance,
            Point clientLocation,
            Integer distance
    ) {
        BooleanBuilder builder = new BooleanBuilder();

        JPAQuery<RestaurantSimpleResponse> query = jpaQueryFactory.select(
                        new QRestaurantSimpleResponse(
                                restaurant.id,
                                restaurant.name,
                                restaurant.address,
                                Boolean.TRUE.equals(isUseDistance) ?
                                        distanceSphereExpression(clientLocation) :
                                        Expressions.nullExpression(Double.class),
                                restaurant.location
                        )
                )
                .from(restaurant)
                .distinct();

        if (Boolean.TRUE.equals(isUseDistance)) {
            RestaurantSearchDistance.contains(distance);

            builder.and(containsExpression(clientLocation, distance));
        } else {
            if (address != null) {
                builder.and(restaurant.address.contains(address));
            }
        }

        if (keyword != null) {
            builder.and(restaurant.name.containsIgnoreCase(keyword).or(menu.name.containsIgnoreCase(keyword)));
            query.leftJoin(restaurant.menus, menu);
        }

        if (minPrice != null && maxPrice != null) {
            builder.and(restaurant.minPrice.loe(maxPrice)).and(restaurant.maxPrice.goe(minPrice));
        }

        if (seatTypeId != null) {
            query.innerJoin(restaurantSeat)
                    .on(restaurantSeat.restaurant.eq(restaurant).and(seatType.id.eq(seatTypeId)));
        }

        List<RestaurantSimpleResponse> records = query.where(builder)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        boolean hasNext = records.size() > pageable.getPageSize();

        if (hasNext) {
            records.remove(records.size() - 1);
        }

        return new SliceImpl<>(records, pageable, hasNext);
    }

    private NumberTemplate<Double> distanceSphereExpression(Point clientLocation) {
        return Expressions.numberTemplate(
                Double.class,
                "ST_DISTANCE_SPHERE({0}, {1})",
                clientLocation, restaurant.location
        );
    }

    public BooleanExpression containsExpression(Point clientLocation, Integer distance) {
        return Expressions.booleanTemplate(
                "ST_CONTAINS(ST_BUFFER({0}, {1}), {2})",
                clientLocation, distance, restaurant.location
        );
    }
}
