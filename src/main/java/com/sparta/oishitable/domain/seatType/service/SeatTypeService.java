package com.sparta.oishitable.domain.seatType.service;

import com.sparta.oishitable.domain.seatType.entity.SeatType;
import com.sparta.oishitable.domain.seatType.repository.SeatTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SeatTypeService {

    private final SeatTypeRepository seatTypeRepository;

    // TODO: orElseThrow 처리
    public SeatType findSeatTypeById(Long seatTypeId) {
        return seatTypeRepository.findById(seatTypeId)
                .orElseThrow();
    }
}
