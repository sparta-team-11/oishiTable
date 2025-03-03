package com.sparta.oishitable.domain.customer.post.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PostKeywordResponse {

    private final Long postId;
    private final Long userId;
    private final Long regionId;
    private final String title;
    private final String content;
    private final String username;
    private final Integer commentCount;
    private final Integer likeCount;
    private final LocalDateTime modifiedAt;

    @QueryProjection
    public PostKeywordResponse(
            Long postId,
            Long userId,
            Long regionId,
            String title,
            String content,
            String username,
            Integer commentCount,
            Integer likeCount,
            LocalDateTime modifiedAt) {
        this.postId = postId;
        this.userId = userId;
        this.regionId = regionId;
        this.title = title;
        this.content = content;
        this.username = username;
        this.commentCount = commentCount;
        this.likeCount = likeCount;
        this.modifiedAt = modifiedAt;
    }
}
