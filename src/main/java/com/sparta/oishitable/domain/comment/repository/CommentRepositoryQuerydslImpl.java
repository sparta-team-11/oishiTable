package com.sparta.oishitable.domain.comment.repository;


import static com.sparta.oishitable.domain.comment.entity.QComment.comment;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.oishitable.domain.comment.dto.response.CommentResponse;
import com.sparta.oishitable.domain.comment.entity.Comment;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CommentRepositoryQuerydslImpl implements CommentRepositoryQuerydsl{

    private final JPAQueryFactory queryFactory;

    @Override
    public List<CommentResponse> findPostComments(Long postId, Long cursorValue, int limit) {

        BooleanBuilder builder = new BooleanBuilder();

        builder.and(comment.post.id.eq(postId))
            .and(comment.parent.isNull());

        if (cursorValue != null) {
            builder.and(comment.id.lt(cursorValue));
        }

        return queryFactory
            .select(
                Projections.constructor(CommentResponse.class,
                    comment.id,
                    comment.post.id,
                    comment.user.id,
                    comment.user.name,
                    comment.content,
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
    public List<CommentResponse> findReplies(Long parentCommentId, Long cursorValue, int limit) {

        BooleanBuilder builder = new BooleanBuilder();

        if (cursorValue != null) {
            builder.and(comment.id.lt(cursorValue));
        }

        builder.and(comment.parent.id.eq(parentCommentId));

        return queryFactory
            .select(
                Projections.constructor(CommentResponse.class,
                    comment.id,
                    comment.post.id,
                    comment.user.id,
                    comment.user.name,
                    comment.content,
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
        return  Optional.ofNullable(queryFactory
            .selectFrom(comment)
            .leftJoin(comment.replies).fetchJoin()
            .where(comment.id.eq(commentId))
            .fetchOne());
    }
}
