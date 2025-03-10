package com.sparta.oishitable.global.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedissonConfig {

    @Value("${spring.data.redis.url}")
    private String redisUrl;

    @Bean
    public RedissonClient redissonClient() {

        String changedUrl = redisUrl.replaceAll("^\\[|\\]$", "");
        Config config = new Config();
        config.useSingleServer().setAddress(changedUrl);
        return Redisson.create(config);
    }
}
