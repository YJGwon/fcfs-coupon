package com.coupop.fcfscoupon.coupon.persistence;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

import com.coupop.fcfscoupon.coupon.model.Coupon;
import com.coupop.fcfscoupon.coupon.model.CouponIssueHistory;
import com.coupop.fcfscoupon.coupon.model.CouponIssueHistoryDetail;
import com.coupop.fcfscoupon.testconfig.MongoDatabaseCleaner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.MongoTemplate;

@DataMongoTest
@Import({MongoCouponIssueHistoryRepository.class, MongoDatabaseCleaner.class})
class MongoIssueHistoryRepositoryTest {

    @Autowired
    private MongoCouponIssueHistoryRepository mongoIssueHistoryRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private MongoDatabaseCleaner cleaner;

    @BeforeEach
    void cleanUp() {
        cleaner.clean();
    }

    @DisplayName("email에 해당하는 document가 없으면 새로운 document에 값을 저장한다.")
    @Test
    void save_ifNoDocument() {
        // given
        final String email = "foo@bar.com";

        // when
        long modifiedCount = mongoIssueHistoryRepository.save(email, createDetail());

        // then
        CouponIssueHistory saved = mongoTemplate.findOne(query(where("email").is(email)), CouponIssueHistory.class);
        assertAll(
                () -> assertThat(modifiedCount).isZero(),
                () -> assertThat(saved.getIssuedCoupons()).hasSize(1)
        );
    }

    @DisplayName("email에 해당하는 document가 있으면 array field에 값을 추가한다.")
    @Test
    void save_ifDocumentExists() {
        // given
        final String email = "foo@bar.com";
        mongoIssueHistoryRepository.save(email, createDetail());

        // when
        long modifiedCount = mongoIssueHistoryRepository.save(email, createDetail());

        // then
        CouponIssueHistory saved = mongoTemplate.findOne(query(where("email").is(email)), CouponIssueHistory.class);
        assertAll(
                () -> assertThat(modifiedCount).isOne(),
                () -> assertThat(saved.getIssuedCoupons()).hasSize(2)
        );
    }

    private CouponIssueHistoryDetail createDetail() {
        final Coupon coupon = new Coupon("fakevalue");
        mongoTemplate.insert(coupon);
        return CouponIssueHistoryDetail.ofNew(coupon);
    }
}
