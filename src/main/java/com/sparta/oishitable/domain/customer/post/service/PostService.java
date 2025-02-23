package com.sparta.oishitable.domain.customer.post.service;

import com.sparta.oishitable.domain.common.user.entity.User;
import com.sparta.oishitable.domain.common.user.repository.UserRepository;
import com.sparta.oishitable.domain.customer.post.dto.request.PostCreateRequest;
import com.sparta.oishitable.domain.customer.post.dto.request.PostUpdateRequest;
import com.sparta.oishitable.domain.customer.post.dto.response.PostKeywordResponse;
import com.sparta.oishitable.domain.customer.post.dto.response. PostRandomResponse;
import com.sparta.oishitable.domain.customer.post.entity.Post;
import com.sparta.oishitable.domain.customer.post.region.entity.Region;
import com.sparta.oishitable.domain.customer.post.region.repository.RegionRepository;
import com.sparta.oishitable.domain.customer.post.repository.PostRepository;
import com.sparta.oishitable.global.exception.ForbiddenException;
import com.sparta.oishitable.global.exception.NotFoundException;
import com.sparta.oishitable.global.exception.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final RegionRepository regionRepository;


    @Transactional
    public Long createPost(Long userId, PostCreateRequest request) {
        User user = findUserById(userId);
        Region region = findRegionById(request.regionId());

        Post post = Post.builder()
                .user(user)
                .region(region)
                .title(request.title())
                .content(request.content())
                .build();

        postRepository.save(post);
        return post.getId();
    }

    public Slice<PostRandomResponse> findPostsByRandom(
            Long userId,
            Long regionId,
            Long cursorValue,
            int limit,
            int randomSeed
    ) {
        List<PostRandomResponse> posts = postRepository.findPostsByRandom(
                userId,
                regionId,
                cursorValue,
                limit,
                randomSeed);

        boolean hasNext = posts.size() == limit;

        return new SliceImpl<>(posts, PageRequest.of(0, limit), hasNext);
    }

    public Slice<PostKeywordResponse> findPostsByKeyword(
            Long userId,
            Long regionId,
            Long cursorValue,
            String keyword,
            int limit
    ) {
        List<PostKeywordResponse> posts = postRepository.findPostsByKeyword(
                userId,
                regionId,
                cursorValue,
                keyword,
                limit);

        boolean hasNext = posts.size() == limit;

        return new SliceImpl<>(posts, PageRequest.of(0, limit), hasNext);
    }

    @Transactional
    public void updatePost(Long userId, Long postId, PostUpdateRequest request) {
        Post post = findPostById(postId);

        isPostOwner(post.getUser().getId(), userId);

        Region region = regionRepository.findById(request.regionId()).orElse(null);

        post.update(request.title(), request.content(), region);
    }

    @Transactional
    public void deletePost(Long postId, Long userId) {
        Post post = findPostById(postId);

        isPostOwner(post.getUser().getId(), userId);

        postRepository.delete(post);
    }

    // 헬퍼

    private User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));
    }

    private Region findRegionById(Long userId) {
        return regionRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.REGION_NOT_FOUND));
    }

    private Post findPostById(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.POST_NOT_FOUND));
    }

    private void isPostOwner(Long postUserId, Long userId) {
        if (!Objects.equals(postUserId, userId)) {
            throw new ForbiddenException(ErrorCode.USER_UNAUTHORIZED);
        }
    }
}
