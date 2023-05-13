package com.coupop.fcfscoupon.percistence;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class RedisCouponCountRepositoryTest {

    @Autowired
    private RedisCouponCountRepository redisCouponCountRepository;

    @Test
    void increaseCount() {
        redisCouponCountRepository.setCount(0);
        assertThat(redisCouponCountRepository.increaseCount()).isEqualTo(1);
    }
}
