package com.sparta.oishitable.domain.customer.restaurant.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberTemplate;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.oishitable.domain.customer.restaurant.dto.response.QRestaurantSimpleResponse;
import com.sparta.oishitable.domain.customer.restaurant.dto.response.RestaurantSimpleResponse;
import com.sparta.oishitable.domain.customer.restaurant.model.RestaurantSearchDistance;
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
            Long seatTypeId,
            Boolean isUseDistance,
            Double clientLat,
            Double clientLon,
            Integer distance
    ) {
        BooleanBuilder builder = new BooleanBuilder();

        NumberTemplate<Double> distanceExpression = Expressions.numberTemplate(
                Double.class,
                "ST_Distance_Sphere(Point({0}, {1}), Point({2}, {3}))",
                clientLon, clientLat, restaurant.longitude, restaurant.latitude
        );

        JPAQuery<RestaurantSimpleResponse> query = jpaQueryFactory.select(
                        new QRestaurantSimpleResponse(
                                restaurant.id,
                                restaurant.name,
                                restaurant.address,
                                Boolean.TRUE.equals(isUseDistance) ? distanceExpression : Expressions.nullExpression(Double.class)
                        )
                )
                .from(restaurant)
                .distinct();

        if (Boolean.TRUE.equals(isUseDistance)) {
            RestaurantSearchDistance.contains(distance);

            builder.and(distanceExpression.loe(distance));
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
            builder.and(seatType.id.eq(seatTypeId));
            query.innerJoin(restaurantSeat)
                    .on(restaurantSeat.restaurant.eq(restaurant))
                    .innerJoin(seatType)
                    .on(restaurantSeat.seatType.eq(seatType));
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
}
