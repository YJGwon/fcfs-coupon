package com.coupop.fcfscoupon.domain.fcfs.testconfig;

import com.coupop.fcfscoupon.domain.fcfs.model.FcfsIssueRepository;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class DataSetup {

    private final StringRedisTemplate redisTemplate;
    private final FcfsIssueRepository fcfsIssueRepository;

    public DataSetup(final StringRedisTemplate redisTemplate,
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
