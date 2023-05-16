package com.coupop.fcfscoupon;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class DatabaseCleanUp {

    private final StringRedisTemplate redisTemplate;

    public DatabaseCleanUp(final StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void execute() {
        redisTemplate.delete(redisTemplate.keys("*"));
    }
}
