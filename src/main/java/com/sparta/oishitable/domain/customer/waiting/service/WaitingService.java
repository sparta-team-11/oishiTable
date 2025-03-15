package com.sparta.oishitable.domain.customer.waiting.service;

import com.sparta.oishitable.domain.customer.waiting.dto.response.WaitingInfoResponse;
import com.sparta.oishitable.domain.customer.waiting.dto.response.WaitingInfosFindResponse;
import com.sparta.oishitable.domain.customer.waiting.repository.WaitingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WaitingService {

    private final WaitingRepository waitingRepository;

    public WaitingInfosFindResponse findWaitingInfos(Long userId, Pageable pageable) {
        Page<WaitingInfoResponse> waitingInfos = waitingRepository.findWaitingInfo(userId, pageable);

        return WaitingInfosFindResponse.from(waitingInfos);
    }
}
