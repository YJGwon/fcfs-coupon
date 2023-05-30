package com.coupop.fcfscoupon.domain.coupon.testconfig;

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
        final Coupon coupon = new Coupon("fakeId");
        couponRepository.insert(coupon);
        return coupon;
    }

    public CouponIssueHistory addHistory(final String couponId, final String email) {
        final CouponIssueHistory history = CouponIssueHistory.ofNew(email, couponId);
        historyRepository.save(history);
        return history;
    }
}
