package com.coupop.fcfscoupon.percistence;

import com.coupop.fcfscoupon.model.CouponEmailRepository;
import java.time.LocalDate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class RedisCouponEmailRepository implements CouponEmailRepository {

    private static final String PATTERN_KEY = "coupon:%s:email";

    private final SetOperations<String, String> couponCountOperations;

    public RedisCouponEmailRepository(final StringRedisTemplate stringRedisTemplate) {
        this.couponCountOperations = stringRedisTemplate.opsForSet();
    }

    @Override
    public Long addEmail(final String email) {
        return couponCountOperations.add(getKey(), email);
    }

    private String getKey() {
        return String.format(PATTERN_KEY, LocalDate.now());
    }
}
