package com.sparta.oishitable.domain.customer.comment.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.oishitable.domain.customer.comment.dto.response.CommentPostResponse;
import com.sparta.oishitable.domain.customer.comment.dto.response.CommentRepliesResponse;
import com.sparta.oishitable.domain.customer.comment.entity.Comment;
import com.sparta.oishitable.domain.customer.comment.entity.QComment;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

import static com.sparta.oishitable.domain.customer.comment.entity.QComment.comment;
import static com.sparta.oishitable.domain.customer.comment.like.entity.QCommentLike.commentLike;

@RequiredArgsConstructor
public class CommentRepositoryQuerydslImpl implements CommentRepositoryQuerydsl {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<CommentPostResponse> findPostComments(Long postId, Long cursorValue, int limit) {

        QComment reply = new QComment("reply");

        BooleanBuilder builder = new BooleanBuilder();

        builder.and(comment.post.id.eq(postId))
                .and(comment.parent.isNull());

        if (cursorValue != null) {
            builder.and(comment.id.lt(cursorValue));
        }

        return queryFactory
                .select(
                        Projections.constructor(CommentPostResponse.class,
                                comment.id,
                                comment.post.id,
                                comment.user.id,
                                comment.user.name,
                                comment.content,
                                JPAExpressions
                                        .select(reply.count().coalesce(0L))
                                        .from(reply)
                                        .where(reply.parent.id.eq(comment.id)),
                                JPAExpressions
                                        .select(commentLike.count().coalesce(0L))
                                        .from(commentLike)
                                        .where(commentLike.comment.id.eq(comment.id)),
                                comment.modifiedAt
                        )
                )
                .from(comment)
                .where(builder)
                .orderBy(comment.createdAt.desc())
                .limit(limit)
                .fetch();
    }

    @Override
    public List<CommentRepliesResponse> findReplies(Long parentCommentId, Long cursorValue, int limit) {

        BooleanBuilder builder = new BooleanBuilder();

        if (cursorValue != null) {
            builder.and(comment.id.lt(cursorValue));
        }

        builder.and(comment.parent.id.eq(parentCommentId));

        return queryFactory
                .select(
                        Projections.constructor(CommentRepliesResponse.class,
                                comment.id,
                                comment.post.id,
                                comment.user.id,
                                comment.user.name,
                                comment.content,
                                JPAExpressions
                                        .select(commentLike.count().coalesce(0L))
                                        .from(commentLike)
                                        .where(commentLike.comment.id.eq(comment.id)),
                                comment.modifiedAt
                        )
                )
                .from(comment)
                .where(builder)
                .orderBy(comment.createdAt.desc())
                .limit(limit)
                .fetch();
    }

    @Override
    public Optional<Comment> findCommentWithRepliesById(Long commentId) {
        return Optional.ofNullable(queryFactory
                .selectFrom(comment)
                .leftJoin(comment.replies).fetchJoin()
                .where(comment.id.eq(commentId))
                .fetchOne());
    }
}
