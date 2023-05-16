package com.coupop.fcfscoupon.percistence;

import com.coupop.fcfscoupon.model.CouponIssuanceRepository;
import java.time.LocalDate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class RedisCouponIssuanceRepository implements CouponIssuanceRepository {

    private static final String PATTERN_KEY = "coupon:%s";

    private final StringRedisTemplate stringRedisTemplate;

    public RedisCouponIssuanceRepository(final StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Override
    public Long add(final String email) {
        return add(email, stringRedisTemplate.opsForSet());
    }

    @Override
    public Long remove(final String email) {
        return remove(email, stringRedisTemplate.opsForSet());
    }

    @Override
    public Long getCount() {
        return getCount(stringRedisTemplate.opsForSet());
    }

    public Long add(final String email, final SetOperations<String, String> setOperations) {
        return setOperations.add(getKey(), email);
    }

    public Long remove(final String email, final SetOperations<String, String> setOperations) {
        return setOperations.remove(getKey(), email);
    }

    public Long getCount(final SetOperations<String, String> setOperations) {
        return setOperations.size(getKey());
    }

    public String getKey() {
        return String.format(PATTERN_KEY, LocalDate.now());
    }
}
