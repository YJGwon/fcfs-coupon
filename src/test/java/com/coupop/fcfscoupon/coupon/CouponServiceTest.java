package com.coupop.fcfscoupon.coupon;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

import com.coupop.fcfscoupon.coupon.model.Coupon;
import com.coupop.fcfscoupon.testconfig.IntegrationTestConfig;
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

    @Test
    void createAndSend() {
        // given
        final String email = "foo@bar.com";

        // when
        couponService.createAndSend(1L, email);

        // then
        final Coupon saved = mongoTemplate.findOne(query(where("value").is(MOCKED_COUPON_VALUE)), Coupon.class);
        assertThat(saved).isNotNull();
    }
}
