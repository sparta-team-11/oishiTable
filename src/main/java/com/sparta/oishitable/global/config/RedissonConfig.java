package com.sparta.oishitable.global.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.ClusterServersConfig;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class RedissonConfig {

    // application.properties에 쉼표로 구분된 노드 정보가 List<String>으로 바인딩됩니다.
    @Value("${spring.data.redis.cluster.nodes}")
    private List<String> clusterNodes;

    @Bean
    public RedissonClient redissonClient() {
        Config config = new Config();
        ClusterServersConfig clusterConfig = config.useClusterServers();

        // List로 받은 각 노드 주소에 "redis://" 접두사가 없는 경우 추가합니다.
        for (String node : clusterNodes) {
            String address = node.trim();
            if (!address.startsWith("redis://")) {
                address = "redis://" + address;
            }
            clusterConfig.addNodeAddress(address);
        }

        clusterConfig.setScanInterval(2000); // 클러스터 토폴로지 갱신 주기(ms)

        return Redisson.create(config);
    }
}
