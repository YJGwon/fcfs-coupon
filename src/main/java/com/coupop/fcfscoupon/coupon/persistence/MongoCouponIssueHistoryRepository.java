package com.coupop.fcfscoupon.coupon.persistence;

import com.coupop.fcfscoupon.coupon.model.CouponIssueHistory;
import com.coupop.fcfscoupon.coupon.model.CouponIssueHistoryRepository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MongoCouponIssueHistoryRepository
        extends CouponIssueHistoryRepository, MongoRepository<CouponIssueHistory, String> {

    @Override
    default CouponIssueHistory save(final CouponIssueHistory history) {
        return this.insert(history);
    }
}
