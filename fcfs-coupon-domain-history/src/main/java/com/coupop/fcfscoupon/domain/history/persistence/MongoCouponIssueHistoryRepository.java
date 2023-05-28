package com.coupop.fcfscoupon.domain.history.persistence;

import com.coupop.fcfscoupon.domain.history.model.CouponIssueHistory;
import com.coupop.fcfscoupon.domain.history.model.CouponIssueHistoryRepository;
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
