package com.coupop.fcfscoupon.api.coupon.testconfig;

import com.coupop.fcfscoupon.domain.coupon.model.Coupon;
import com.coupop.fcfscoupon.domain.coupon.model.CouponRepository;
import com.coupop.fcfscoupon.domain.history.model.CouponIssueHistory;
import com.coupop.fcfscoupon.domain.history.model.CouponIssueHistoryRepository;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

@Component
public class DataSetup {

    private final MongoTemplate mongoTemplate;
    private final CouponRepository couponRepository;
    private final CouponIssueHistoryRepository historyRepository;

    public DataSetup(final MongoTemplate mongoTemplate,
                     final CouponRepository couponRepository,
                     final CouponIssueHistoryRepository historyRepository) {
        this.mongoTemplate = mongoTemplate;
        this.couponRepository = couponRepository;
        this.historyRepository = historyRepository;
    }

    public void clean() {
        for (String collectionName : mongoTemplate.getCollectionNames()) {
            mongoTemplate.dropCollection(collectionName);
        }
    }

    public Coupon addCoupon() {
        final Coupon coupon = new Coupon("fakeValue");
        couponRepository.insert(coupon);
        return coupon;
    }

    public CouponIssueHistory addHistory(final String couponId) {
        final CouponIssueHistory history = CouponIssueHistory.ofNew("foo@bar.com", couponId);
        historyRepository.save(history);
        return history;
    }
}
