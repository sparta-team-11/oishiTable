package com.sparta.oishitable.domain.seatType.service;

import com.sparta.oishitable.domain.seatType.entity.SeatType;
import com.sparta.oishitable.domain.seatType.repository.SeatTypeRepository;
import com.sparta.oishitable.global.exception.CustomRuntimeException;
import com.sparta.oishitable.global.exception.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SeatTypeService {

    private final SeatTypeRepository seatTypeRepository;

    public SeatType findSeatTypeById(Long seatTypeId) {
        return seatTypeRepository.findById(seatTypeId)
                .orElseThrow(() -> new CustomRuntimeException(ErrorCode.SEAT_TYPE_NOT_FOUND));
    }
}
