package com.sparta.oishitable.global.config;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.List;

@Slf4j
@Configuration
public class RedisConfig {

    // cluster 노드 목록을 리스트로 바인딩
    @Value("${spring.data.redis.cluster.nodes}")
    private List<String> clusterNodes;

    @PostConstruct
    public void init() {
        log.info("Cluster nodes: {}", clusterNodes);
    }

    @Value("${spring.data.redis.cluster.max-redirects}")
    private int maxRedirects;

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {

        // 클러스터 설정 구성
        RedisClusterConfiguration clusterConfiguration = new RedisClusterConfiguration(clusterNodes);
        clusterConfiguration.setMaxRedirects(maxRedirects);

        // LettuceConnectionFactory에 클러스터 설정 주입
        return new LettuceConnectionFactory(clusterConfiguration);
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new StringRedisSerializer());
        return template;
    }
}
