package com.sparta.oishitable.domain.customer.post.repository;

import com.sparta.oishitable.domain.customer.post.dto.response.PostCounts;
import com.sparta.oishitable.domain.customer.post.dto.response.PostKeywordResponse;
import com.sparta.oishitable.domain.customer.post.dto.response.PostRandomResponse;
import com.sparta.oishitable.domain.customer.post.entity.Post;

import java.util.List;

public interface PostRepositoryQuerydsl {

    List<PostRandomResponse> findPostsByRandom(Long userId, Long regionId, Long cursorValue, int limit, int randomSeed);

    List<PostKeywordResponse> findPostsByKeyword(Long userId, Long regionId, Long cursorValue, String keyword, int limit);

    List<PostCounts> findAllByIdWithCommentsAndLikes(List<Long> postIds);
}
