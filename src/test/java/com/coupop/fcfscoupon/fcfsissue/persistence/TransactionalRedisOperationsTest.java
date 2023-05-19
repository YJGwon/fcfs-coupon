package com.coupop.fcfscoupon.fcfsissue.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import com.coupop.fcfscoupon.config.EmbeddedRedisStarter;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.redis.DataRedisTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.core.StringRedisTemplate;

@DataRedisTest
@Import({TransactionalRedisOperations.class, EmbeddedRedisStarter.class})
class TransactionalRedisOperationsTest {

    @Autowired
    private TransactionalRedisOperations transactionalRedisOperations;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @DisplayName("Transaction 안에서 값을 조회한 뒤 결과를 조회할 수 있다.")
    @Test
    void getValue_in_transaction() {
        stringRedisTemplate.opsForValue().set("key", "value");

        final List<Object> result = transactionalRedisOperations.startSession()
                .multi()
                .andThen(operations -> operations.opsForValue().get("key"))
                .exec();
        assertThat(result).containsExactly("value");
    }
}
