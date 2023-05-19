package com.coupop.fcfscoupon.coupon;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

import com.coupop.fcfscoupon.coupon.model.Coupon;
import com.coupop.fcfscoupon.coupon.model.CouponIssueHistory;
import com.coupop.fcfscoupon.testconfig.IntegrationTestConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;

@SpringBootTest
class CouponServiceTest extends IntegrationTestConfig {

    @Autowired
    private CouponService couponService;

    @Autowired
    private MongoTemplate mongoTemplate;

    @DisplayName("쿠폰을 생성하여 저장하고 쿠폰 발송 내역을 저장한 뒤 이메일을 발송한다.")
    @Test
    void createAndSend() {
        // given
        final String email = "yj970125@gmail.com";

        // when
        couponService.createAndSend(1L, email);

        // then
        final Coupon savedCoupon = mongoTemplate.findOne(query(where("value").is(MOCKED_COUPON_VALUE)), Coupon.class);
        final CouponIssueHistory savedHistory =
                mongoTemplate.findOne(query(where("email").is(email)), CouponIssueHistory.class);

        assertAll(
                () -> assertThat(savedCoupon).isNotNull(),
                () -> assertThat(savedHistory).isNotNull(),
                () -> verify(couponEmailSender).send(any(Coupon.class), eq(email))
        );
    }
}
