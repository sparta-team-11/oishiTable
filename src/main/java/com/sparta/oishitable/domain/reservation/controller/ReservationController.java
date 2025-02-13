package com.sparta.oishitable.domain.reservation.controller;

import com.sparta.oishitable.domain.reservation.dto.ReservationCreateRequest;
import com.sparta.oishitable.domain.reservation.service.ReservationService;
import com.sparta.oishitable.domain.user.entity.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
public class ReservationController {
    private final ReservationService reservationService;

    @PostMapping
    public ResponseEntity<Void> createReservation(
            @AuthenticationPrincipal User user, //spring security에 관해 자세히 몰라 User user만 만들어 놓음
            @Valid @RequestBody ReservationCreateRequest request
    ) {

        reservationService.createReservationService(user,request);

        return new ResponseEntity<>(HttpStatus.OK);

    }
}
