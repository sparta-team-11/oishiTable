package com.sparta.oishitable.domain.customer.post.repository;

import com.sparta.oishitable.domain.customer.post.dto.response.PostKeywordResponse;
import com.sparta.oishitable.domain.customer.post.dto.response.PostRandomResponse;

import java.util.List;

public interface PostRepositoryQuerydsl {

    List<PostRandomResponse> findAllPosts(Long userId, Long regionId, Long cursorValue, int limit, int randomSeed);

    List<PostKeywordResponse> getPostsByKeyword(Long userId, Long regionId, Long cursorValue, String keyword, int limit);
}
