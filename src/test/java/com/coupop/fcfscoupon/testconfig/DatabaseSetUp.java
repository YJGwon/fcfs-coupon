package com.coupop.fcfscoupon.testconfig;

import com.coupop.fcfscoupon.fcfsissue.model.FcfsIssueRepository;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class DatabaseSetUp {

    private final StringRedisTemplate redisTemplate;
    private final FcfsIssueRepository fcfsIssueRepository;

    public DatabaseSetUp(final StringRedisTemplate redisTemplate,
                         final FcfsIssueRepository fcfsIssueRepository) {
        this.redisTemplate = redisTemplate;
        this.fcfsIssueRepository = fcfsIssueRepository;
    }

    public void clean() {
        redisTemplate.delete(redisTemplate.keys("*"));
    }

    public void setCount(int count) {
        for (int i = 0; i < count; i++) {
            fcfsIssueRepository.add(String.format("foo%d@bar.com", i));
        }
    }
}
