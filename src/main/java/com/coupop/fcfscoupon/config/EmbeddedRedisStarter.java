package com.coupop.fcfscoupon.config;

import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import redis.embedded.RedisServer;

@Component
public class EmbeddedRedisStarter {

    private final RedisServer redisServer;

    public EmbeddedRedisStarter(@Value("${spring.data.redis.port}") final int redisPort) {
        this.redisServer = new RedisServer(redisPort);
        redisServer.start();
    }

    @PreDestroy
    private void stopRedis() {
        if (redisServer != null) {
            redisServer.stop();
        }
    }
}
