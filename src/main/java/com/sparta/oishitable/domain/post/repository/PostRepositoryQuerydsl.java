package com.sparta.oishitable.domain.post.repository;

import com.sparta.oishitable.domain.post.dto.response.PostResponse;
import java.util.List;

public interface PostRepositoryQuerydsl {

    List<PostResponse> findAllPosts(Long userId, Long regionId, Long cursorId, int limit);

    List<PostResponse> getPostsByKeyword(Long userId, Long regionId, Long cursorId, String keyword, int limit);
}
