package com.coupop.fcfscoupon.api.testconfig;

import com.coupop.fcfscoupon.domain.fcfs.model.FcfsIssueRepository;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class DatabaseSetUp {

    private final StringRedisTemplate redisTemplate;
    private final FcfsIssueRepository fcfsIssueRepository;
    private final MongoDatabaseCleaner mongoDatabaseCleaner;

    public DatabaseSetUp(final StringRedisTemplate redisTemplate,
                         final FcfsIssueRepository fcfsIssueRepository,
                         final MongoDatabaseCleaner mongoDatabaseCleaner) {
        this.redisTemplate = redisTemplate;
        this.fcfsIssueRepository = fcfsIssueRepository;
        this.mongoDatabaseCleaner = mongoDatabaseCleaner;
    }

    public void cleanRedis() {
        redisTemplate.delete(redisTemplate.keys("*"));
        mongoDatabaseCleaner.clean();
    }

    public void setCount(int count) {
        for (int i = 0; i < count; i++) {
            fcfsIssueRepository.add(String.format("foo%d@bar.com", i));
        }
    }
}
