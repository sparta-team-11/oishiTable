package com.sparta.oishitable.domain.customer.reservation.controller;

import com.sparta.oishitable.domain.customer.reservation.dto.ReservationCreateRequest;
import com.sparta.oishitable.domain.customer.reservation.dto.ReservationFindResponse;
import com.sparta.oishitable.domain.customer.reservation.service.ReservationService;
import com.sparta.oishitable.global.security.entity.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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
        reservationService.createReservationService(userDetails.getId(), request);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{reservationId}")
    public ResponseEntity<ReservationFindResponse> findReservation(
            @PathVariable Long reservationId
    ) {
        return new ResponseEntity<>(reservationService.findReservation(reservationId), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<ReservationFindResponse>> findAllReservations(
            @RequestParam Long userId
    ) {
        List<ReservationFindResponse> reservationResponses = reservationService.findAllReservations(userId);

        return new ResponseEntity<>(reservationResponses, HttpStatus.OK);
    }

    @DeleteMapping("/{reservationId}")
    public ResponseEntity<Void> deleteReservation(
            @PathVariable Long reservationId
    ) {
        reservationService.deleteReservation(reservationId);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}



