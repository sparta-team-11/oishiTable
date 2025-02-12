package com.sparta.oishitable.domain.seatType.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "seat_types")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SeatType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seat_type_id")
    private Long id;

    @Column(nullable = false)
    private String name;
}
