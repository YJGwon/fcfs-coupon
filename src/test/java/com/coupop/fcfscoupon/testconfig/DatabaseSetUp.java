package com.coupop.fcfscoupon.testconfig;

import com.coupop.fcfscoupon.model.CouponIssuanceRepository;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class DatabaseSetUp {

    private final StringRedisTemplate redisTemplate;
    private final CouponIssuanceRepository couponIssuanceRepository;

    public DatabaseSetUp(final StringRedisTemplate redisTemplate,
                         final CouponIssuanceRepository couponIssuanceRepository) {
        this.redisTemplate = redisTemplate;
        this.couponIssuanceRepository = couponIssuanceRepository;
    }

    public void clean() {
        redisTemplate.delete(redisTemplate.keys("*"));
    }

    public void setCount(int count) {
        for (int i = 0; i < count; i++) {
            couponIssuanceRepository.add(String.format("foo%d@bar.com", i));
        }
    }
}
