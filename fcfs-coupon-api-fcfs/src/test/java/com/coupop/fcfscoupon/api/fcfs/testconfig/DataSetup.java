package com.coupop.fcfscoupon.api.fcfs.testconfig;

import com.coupop.fcfscoupon.domain.fcfs.model.FcfsIssueRepository;
import com.coupop.fcfscoupon.domain.history.model.CouponIssueHistory;
import com.coupop.fcfscoupon.domain.history.model.CouponIssueHistoryRepository;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class DataSetup {

    private final StringRedisTemplate redisTemplate;
    private final FcfsIssueRepository fcfsIssueRepository;
    private final MongoDatabaseCleaner mongoDatabaseCleaner;
    private final CouponIssueHistoryRepository couponIssueHistoryRepository;

    public DataSetup(final StringRedisTemplate redisTemplate,
                     final FcfsIssueRepository fcfsIssueRepository,
                     final MongoDatabaseCleaner mongoDatabaseCleaner,
                     final CouponIssueHistoryRepository couponIssueHistoryRepository) {
        this.redisTemplate = redisTemplate;
        this.fcfsIssueRepository = fcfsIssueRepository;
        this.mongoDatabaseCleaner = mongoDatabaseCleaner;
        this.couponIssueHistoryRepository = couponIssueHistoryRepository;
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

    public CouponIssueHistory addHistory(final String email) {
        final CouponIssueHistory history = CouponIssueHistory.ofNew("foo@bar.com", "fakeId");
        couponIssueHistoryRepository.save(history);
        return history;
    }
}
