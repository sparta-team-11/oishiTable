package com.sparta.oishitable.domain.common.user.repository;

import com.sparta.oishitable.domain.owner.restaurant.waiting.dto.response.WaitingUserDetails;

import java.util.List;

public interface UserQRepository {

    List<WaitingUserDetails> findWaitingUserDetails(List<Long> userIds);
}
