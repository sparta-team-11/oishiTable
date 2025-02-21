package com.sparta.oishitable;

import com.sparta.oishitable.domain.admin.seatType.entity.SeatType;
import com.sparta.oishitable.domain.admin.seatType.repository.SeatTypeRepository;
import com.sparta.oishitable.domain.auth.dto.request.AuthSignupRequest;
import com.sparta.oishitable.domain.auth.service.AuthService;
import com.sparta.oishitable.domain.common.user.entity.UserRole;
import com.sparta.oishitable.domain.common.user.repository.UserRepository;
import com.sparta.oishitable.domain.owner.restaurant.entity.Restaurant;
import com.sparta.oishitable.domain.owner.restaurant.repository.RestaurantRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalTime;
import java.util.List;

@SpringBootTest
public class InitRecordTest {

    @Autowired
    AuthService authService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    RestaurantRepository restaurantRepository;
    @Autowired
    SeatTypeRepository seatTypeRepository;

    @Test
//    @Transactional
    void createUsers() {
        int cnt = 10;
        int userCnt = 7;
        int ownerCnt = cnt - userCnt;

        for (int i = 0; i < userCnt; i++) {
            String email = "user" + i + "@user.com";
            String password = "user" + i;
            String name = "user" + i;
            String phoneNumber = String.format("010-%04d-1234", i);
            authService.signup(new AuthSignupRequest(email, password, name, phoneNumber, UserRole.CUSTOMER));
        }

        for (int i = 0; i < ownerCnt; i++) {
            String email = "owner" + i + "@owner.com";
            String password = "owner" + i;
            String name = "owner" + i;
            String phoneNumber = String.format("010-1234-%04d", i);
            authService.signup(new AuthSignupRequest(email, password, name, phoneNumber, UserRole.OWNER));
        }
    }

    @Test
//    @Transactional
    void createRestaurants() {
        int cnt = 11;

        for (int i = 8; i < cnt; i++) {
            Restaurant restaurant = Restaurant.builder()
                    .name("restaurant" + i)
                    .owner(userRepository.findById((long) i).orElseThrow())
                    .location("서울")
                    .openTime(LocalTime.parse("11:00"))
                    .closeTime(LocalTime.parse("22:00"))
                    .breakTimeStart(LocalTime.parse("15:00"))
                    .breakTimeEnd(LocalTime.parse("18:00"))
                    .introduce("소개 소개")
                    .deposit(20000)
                    .reservationInterval(LocalTime.parse("00:30"))
                    .address("서울")
                    .latitude(0.12341234)
                    .longitude(0.56785678)
                    .build();

            restaurantRepository.save(restaurant);
        }
    }

    @Test
    void createSeatType() {
        seatTypeRepository.saveAll(List.of(
                SeatType.builder()
                        .name("테이블")
                        .build(),
                SeatType.builder()
                        .name("홀")
                        .build(),
                SeatType.builder()
                        .name("바")
                        .build()));
    }
}