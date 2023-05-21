package com.coupop.fcfscoupon.coupon.persistence;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

import com.coupop.fcfscoupon.coupon.model.CouponIssueHistory;
import com.coupop.fcfscoupon.coupon.model.CouponIssueHistoryDetail;
import com.coupop.fcfscoupon.coupon.model.CouponIssueHistoryRepository;
import com.mongodb.client.result.UpdateResult;
import java.util.Optional;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

@Repository
public class MongoCouponIssueHistoryRepository implements CouponIssueHistoryRepository {

    private final MongoTemplate mongoTemplate;

    public MongoCouponIssueHistoryRepository(final MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public long save(final String email, final CouponIssueHistoryDetail detail) {
        UpdateResult result = mongoTemplate.update(CouponIssueHistory.class)
                .matching(query(where("email").is(email)))
                .apply(new Update().push("issuedCoupons", detail))
                .upsert();
        return result.getModifiedCount();
    }

    @Override
    public Optional<CouponIssueHistory> findByEmail(final String email) {
        final CouponIssueHistory found = mongoTemplate
                .findOne(query(where("email").is(email)), CouponIssueHistory.class);
        return Optional.ofNullable(found);
    }
}
