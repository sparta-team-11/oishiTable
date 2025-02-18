package com.sparta.oishitable.domain.customer.reservation.controller;

import com.sparta.oishitable.domain.customer.coupon.entity.Coupon;
import com.sparta.oishitable.domain.customer.reservation.dto.ReservationCreateRequest;
import com.sparta.oishitable.domain.customer.reservation.dto.ReservationFindResponse;
import com.sparta.oishitable.domain.customer.reservation.service.ReservationService;
import com.sparta.oishitable.global.security.entity.CustomUserDetails;
import com.sparta.oishitable.global.util.UriBuilderUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/customer/api/reservations")
@RequiredArgsConstructor
public class ReservationController {
    private final ReservationService reservationService;

    @PostMapping
    public ResponseEntity<Void> createReservation(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody @Valid ReservationCreateRequest request


    ) {

        Long reservationId = reservationService.createReservationService(userDetails.getId(), request);

        String path = "/customer/api/reservations/{reservationId}";
        URI uri = UriBuilderUtil.create(path, reservationId);

        return ResponseEntity.created(uri).build();
    }

    @GetMapping("/{reservationId}")
    public ResponseEntity<ReservationFindResponse> findReservation(
            @PathVariable long reservationId
    ) {
        return ResponseEntity.ok(reservationService.findReservation(reservationId));

    }

    @GetMapping
    public ResponseEntity<List<ReservationFindResponse>> findAllReservations(
            @RequestParam Long userId
    ) {
        List<ReservationFindResponse> reservationResponses = reservationService.findAllReservations(userId);


        return ResponseEntity.ok(reservationResponses);

    }

    @DeleteMapping("/{reservationId}")
    public ResponseEntity<Void> deleteReservation(
            @PathVariable long reservationId
    ) {
        reservationService.deleteReservation(reservationId);

        return ResponseEntity.noContent().build();
    }
}



