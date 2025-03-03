package com.sparta.oishitable.domain.owner.restaurant.service;

import com.sparta.oishitable.domain.common.auth.service.AuthService;
import com.sparta.oishitable.domain.common.user.entity.User;
import com.sparta.oishitable.domain.common.user.service.UserService;
import com.sparta.oishitable.domain.owner.restaurant.dto.request.RestaurantCreateRequest;
import com.sparta.oishitable.domain.owner.restaurant.dto.request.RestaurantProfileUpdateRequest;
import com.sparta.oishitable.domain.owner.restaurant.dto.response.RestaurantFindResponse;
import com.sparta.oishitable.domain.owner.restaurant.entity.Restaurant;
import com.sparta.oishitable.domain.owner.restaurant.repository.OwnerRestaurantRepository;
import com.sparta.oishitable.domain.owner.restaurantseat.service.RestaurantSeatService;
import com.sparta.oishitable.global.exception.CustomRuntimeException;
import com.sparta.oishitable.global.exception.NotFoundException;
import com.sparta.oishitable.global.exception.error.ErrorCode;
import com.sparta.oishitable.global.util.geocode.GeocodingClient;
import com.sparta.oishitable.global.util.geocode.GeocodingResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class OwnerRestaurantService {

    private final UserService userService;
    private final AuthService authService;
    private final RestaurantSeatService restaurantSeatService;
    private final OwnerRestaurantRepository restaurantRepository;
    private final GeocodingClient geocodingClient;

    @Transactional
    public Long createRestaurant(Long userId, RestaurantCreateRequest restaurantCreateRequest) {
        User owner = userService.findUserById(userId);

        Mono<GeocodingResponse.Document> coordinatesResult = findCoordinates(restaurantCreateRequest.address());
        GeocodingResponse.Document coordinates = coordinatesResult.block();

        Restaurant restaurant = restaurantCreateRequest.toEntity(owner, coordinates.latitude(), coordinates.longitude());
        Restaurant savedRestaurant = restaurantRepository.save(restaurant);

        restaurantSeatService.createAllRestaurantSeat(
                savedRestaurant,
                restaurantCreateRequest.restaurantSeatCreateRequestList()
        );

        return savedRestaurant.getId();
    }

    public RestaurantFindResponse findRestaurant(Long restaurantId) {
        Restaurant restaurant = findById(restaurantId);

        return RestaurantFindResponse.from(restaurant);
    }

    @Transactional
    public void updateRestaurantProfile(
            Long userId,
            Long restaurantId,
            RestaurantProfileUpdateRequest restaurantProfileUpdateRequest
    ) {
        Restaurant restaurant = findById(restaurantId);

        authService.checkUserAuthority(restaurant.getOwner().getId(), userId);

        restaurant.updateProfile(
                restaurantProfileUpdateRequest.name(),
                restaurantProfileUpdateRequest.introduce(),
                restaurantProfileUpdateRequest.deposit()
        );
    }

    @Transactional
    public void deleteRestaurant(Long userId, Long restaurantId) {
        Restaurant restaurant = findById(restaurantId);

        authService.checkUserAuthority(restaurant.getOwner().getId(), userId);

        restaurantSeatService.deleteAllRestaurantSeat(restaurant);
        restaurantRepository.delete(restaurant);
    }

    public Restaurant findById(Long restaurantId) {
        return restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.RESTAURANT_NOT_FOUND));
    }

    public Mono<GeocodingResponse.Document> findCoordinates(String address) {
        return geocodingClient.findGeocoding(address)
                .flatMap(geocodingResponse -> {
                    if (geocodingResponse != null && geocodingResponse.hasResult()) {
                        return Mono.just(geocodingResponse.findFirstResult()
                                .orElseThrow(() -> new CustomRuntimeException(ErrorCode.GEOCODING_NO_RESULT)));
                    } else {
                        return Mono.error(new CustomRuntimeException(ErrorCode.GEOCODING_NO_RESULT));
                    }
                })
                .onErrorResume(e -> {
                    log.error("Error while finding coordinates: {}", e.getMessage());
                    return Mono.error(new CustomRuntimeException(ErrorCode.GEOCODING_API_ERROR));
                });
    }
}
