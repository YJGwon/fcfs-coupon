package com.coupop.fcfscoupon.percistence;

import com.coupop.fcfscoupon.model.CouponCountRepository;
import java.time.LocalDate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

@Repository
public class RedisCouponCountRepository implements CouponCountRepository {

    private static final String ERROR_NOT_INITIALIZED = "쿠폰 수량이 초기화되지 않았습니다.";
    private static final String KEY_PREFIX = "coupon:";

    private final ValueOperations<String, String> couponCountOperations;

    public RedisCouponCountRepository(final StringRedisTemplate stringRedisTemplate) {
        this.couponCountOperations = stringRedisTemplate.opsForValue();
    }

    @Override
    public void increaseCount() {
        couponCountOperations.increment(getKey());
    }

    @Override
    public int getCount() {
        String count = couponCountOperations.get(getKey());
        if (count == null) {
            throw new IllegalArgumentException(ERROR_NOT_INITIALIZED);
        }
        return Integer.parseInt(count);
    }

    @Override
    public void setCount(final int count) {
        couponCountOperations.set(getKey(), Integer.toString(count));
    }

    private String getKey() {
        return KEY_PREFIX + LocalDate.now();
    }
}
