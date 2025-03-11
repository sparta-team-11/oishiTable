package com.sparta.oishitable.domain.customer.post.entity;

import com.sparta.oishitable.domain.common.user.entity.User;
import com.sparta.oishitable.domain.customer.post.region.entity.Region;
import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.LocalDateTime;

@Getter
@Document(indexName = "posts")
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class PostDocument {

    @Id
    private String id;

    @Field(name = "post_id", type = FieldType.Long)
    private Long postId;

    @Field(name = "user_id", type = FieldType.Long)
    private Long userId;

    @Field(name = "region_id", type = FieldType.Long)
    private Long regionId;

    @Field(type = FieldType.Text)
    private String title;

    @Field(name = "region_name", type = FieldType.Text)
    private String regionName;

    @Field(type = FieldType.Text)
    private String nickname;

    @Field(type = FieldType.Text)
    private String content;

    @Field(name = "modified_at", type = FieldType.Date,
    format = DateFormat.basic_date_time_no_millis, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime modifiedAt;

    public static PostDocument from(Post post, Region region, User user) {

        String id = String.valueOf(post.getId());
        return new PostDocument(
                id,
                post.getId(),
                user.getId(),
                region.getId(),
                post.getTitle(),
                region.getName(),
                user.getNickname(),
                post.getContent(),
                post.getModifiedAt()
        );
    }
}
