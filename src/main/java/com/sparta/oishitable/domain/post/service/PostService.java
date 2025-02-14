package com.sparta.oishitable.domain.post.service;

import com.sparta.oishitable.domain.common.Region;
import com.sparta.oishitable.domain.common.RegionRepository;
import com.sparta.oishitable.domain.post.dto.request.PostCreateRequest;
import com.sparta.oishitable.domain.post.dto.request.PostUpdateRequest;
import com.sparta.oishitable.domain.post.dto.response.FeedKeywordResponse;
import com.sparta.oishitable.domain.post.dto.response.FeedRandomResponse;
import com.sparta.oishitable.domain.post.dto.response.PostKeywordResponse;
import com.sparta.oishitable.domain.post.dto.response.PostRandomResponse;
import com.sparta.oishitable.domain.post.entity.Post;
import com.sparta.oishitable.domain.post.repository.PostRepository;
import com.sparta.oishitable.domain.user.entity.User;
import com.sparta.oishitable.domain.user.repository.UserRepository;
import com.sparta.oishitable.global.exception.CustomRuntimeException;
import com.sparta.oishitable.global.exception.error.ErrorCode;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final RegionRepository regionRepository;


    @Transactional
    public void create(PostCreateRequest request) {

        User user = findUserById(request.userId());
        Region region = findRegionById(request.regionId());

        Post post = Post.builder()
            .user(user)
            .region(region)
            .title(request.title())
            .content(request.content())
            .build();

        postRepository.save(post);
    }

    public FeedRandomResponse getAllPosts(
        Long userId,
        Long regionId,
        Long cursorValue,
        int limit,
        int randomSeed) {

        List<PostRandomResponse> posts = postRepository.findAllPosts(
            userId,
            regionId,
            cursorValue,
            limit,
            randomSeed);

        Long nextCursor = null;
        boolean hasMore = false;

        if (posts.size() == limit) {
            nextCursor = posts.get(posts.size() - 1).randomValue();
            hasMore = true; // 조회된 개수가 limit이면 다음 페이지 있으면 true 없으면 false 반환
        }

        return new FeedRandomResponse(posts, randomSeed, nextCursor, hasMore);
    }

    public FeedKeywordResponse getPostsByKeyword(
        Long userId,
        Long regionId,
        Long cursorValue,
        String keyword,
        int limit) {

        List<PostKeywordResponse> posts = postRepository.getPostsByKeyword(
            userId,
            regionId,
            cursorValue,
            keyword,
            limit);

        // 조회된 게시글 수가 limit과 동일하면 마지막 게시글의 id를 커서로 사용
        // 그렇지 않으면 더 이상 데이터가 없음을 의미하므로 null
        Long nextCursor = null;
        if (posts.size() == limit) {
            nextCursor = posts.get(posts.size() - 1).postId();
        }

        return new FeedKeywordResponse(posts, nextCursor);
    }

    @Transactional
    public void update(Long postId, PostUpdateRequest request) {

        Post post = findPostById(postId);

        validateUser(post.getUser().getId(), request.userId());

        Region region = regionRepository.findById(request.regionId()).orElse(null);

        post.update(request.title(), request.content(), region);
    }

    @Transactional
    public void delete(Long postId, Long userId) {

        Post post = findPostById(postId);

        validateUser(post.getUser().getId(), userId);

        postRepository.delete(post);
    }

    // 헬퍼

    private User findUserById(Long userId) {
        return userRepository.findById(userId)
            .orElseThrow(() -> new CustomRuntimeException(ErrorCode.USER_NOT_FOUND));
    }

    private Region findRegionById(Long userId) {
        return regionRepository.findById(userId)
            .orElseThrow(() -> new CustomRuntimeException(ErrorCode.USER_NOT_FOUND));
    }

    private Post findPostById(Long postId) {
        return postRepository.findById(postId)
            .orElseThrow(() -> new CustomRuntimeException(ErrorCode.POST_NOT_FOUND));
    }

    private void validateUser(Long postUserId, Long userId) {
        if (!Objects.equals(postUserId, userId)) {
            throw new CustomRuntimeException(ErrorCode.USER_UNAUTHORIZED);
        }
    }
}
