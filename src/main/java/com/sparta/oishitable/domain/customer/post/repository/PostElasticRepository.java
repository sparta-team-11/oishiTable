package com.sparta.oishitable.domain.customer.post.repository;

import com.sparta.oishitable.domain.customer.post.entity.PostDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface PostElasticRepository extends ElasticsearchRepository<PostDocument, String> {

}
