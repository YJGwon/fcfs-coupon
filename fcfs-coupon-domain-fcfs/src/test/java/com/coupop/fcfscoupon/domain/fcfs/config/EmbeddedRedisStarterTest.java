package com.coupop.fcfscoupon.domain.fcfs.config;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.redis.DataRedisTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

@DataRedisTest
@Import(EmbeddedRedisStarter.class)
public class EmbeddedRedisStarterTest {

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @DisplayName("Embedded Redis에 연결한다.")
    @Test
    void connection() {
        final ValueOperations<String, String> valueOperations = stringRedisTemplate.opsForValue();
        valueOperations.set("key", "value");

        assertThat(valueOperations.get("key")).isEqualTo("value");
    }
}
