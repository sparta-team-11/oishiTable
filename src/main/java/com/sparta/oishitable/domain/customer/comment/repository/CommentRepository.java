package com.sparta.oishitable.domain.customer.comment.repository;

import com.sparta.oishitable.domain.customer.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long>, CommentRepositoryQuerydsl {
}
