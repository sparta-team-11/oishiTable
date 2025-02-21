package com.sparta.oishitable.global.util.geocode;

import com.sparta.oishitable.domain.common.user.entity.User;
import com.sparta.oishitable.domain.common.user.repository.UserRepository;
import com.sparta.oishitable.domain.owner.restaurant.dto.request.RestaurantCreateRequest;
import com.sparta.oishitable.domain.owner.restaurant.entity.Restaurant;
import com.sparta.oishitable.domain.owner.restaurant.repository.RestaurantRepository;
import com.sparta.oishitable.domain.owner.restaurant.service.OwnerRestaurantService;
import com.sparta.oishitable.global.util.geocode.GeocodingResponse.Document;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import reactor.core.publisher.Mono;

import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
public class OwnerRestaurantServiceGeocodingTest {

    @Autowired
    private OwnerRestaurantService ownerRestaurantService;

    @MockitoBean
    private RestaurantRepository restaurantRepository;

    @MockitoBean
    private UserRepository userRepository;

    @MockitoBean
    private GeocodingClient geocodingClient;

    @Test
    public void 가게_생성_성공_테스트() {
        // Given
        Long userId = 1L;
        String address = "서울특별시 강남구 역삼동";

        RestaurantCreateRequest restaurantCreateRequest = new RestaurantCreateRequest(
                userId,
                "테스트 식당",
                address,
                LocalTime.of(9, 0),
                LocalTime.of(22, 0),
                LocalTime.of(15, 0),
                LocalTime.of(17, 0),
                "테스트 소개",
                10000,
                LocalTime.of(1, 0),
                Collections.emptyList()
        );

        User owner = User.builder()
                .id(userId)
                .email("owner@email.com")
                .password("password")
                .name("테스트 오너")
                .phoneNumber("01012345678")
                .build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(owner));

        GeocodingResponse mockResponse = GeocodingResponse.from(List.of(
                Document.from(address, 127.039, 37.501)
        ));
        when(geocodingClient.findGeocoding(anyString())).thenReturn(Mono.just(mockResponse));

        Restaurant restaurant = restaurantCreateRequest.toEntity(owner);
        restaurant.getAddress();
        restaurant.getLatitude();
        restaurant.getLongitude();

        when(restaurantRepository.save(org.mockito.Mockito.any(Restaurant.class))).thenReturn(restaurant);

        // When
        Long restaurantId = ownerRestaurantService.createRestaurant(restaurantCreateRequest);

        // Then
        assertEquals(restaurant.getId(), restaurantId);
    }
}