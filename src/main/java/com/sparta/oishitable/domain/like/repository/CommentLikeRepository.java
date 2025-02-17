package com.sparta.oishitable.domain.like.repository;

import com.sparta.oishitable.domain.comment.entity.Comment;
import com.sparta.oishitable.domain.like.entity.CommentLike;
import com.sparta.oishitable.domain.user.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {

    boolean existsByCommentAndUser(Comment comment, User user);

    Optional<CommentLike> findCommentLikeByCommentAndUser(Comment comment, User user);

    Integer countCommentLikeByCommentId(Long commentId);

}
