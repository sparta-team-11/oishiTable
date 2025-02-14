package com.sparta.oishitable.domain.post.repository;

import com.sparta.oishitable.domain.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long>, PostRepositoryQuerydsl {



}
