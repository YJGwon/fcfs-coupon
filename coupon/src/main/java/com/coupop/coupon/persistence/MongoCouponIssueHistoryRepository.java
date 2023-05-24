package com.coupop.coupon.persistence;

import com.coupop.coupon.model.CouponIssueHistory;
import com.coupop.coupon.model.CouponIssueHistoryRepository;
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
