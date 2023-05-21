package com.coupop.fcfscoupon.coupon;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

import com.coupop.fcfscoupon.coupon.dto.HistoryRequest;
import com.coupop.fcfscoupon.coupon.dto.HistoryResponse;
import com.coupop.fcfscoupon.coupon.exception.HistoryNotFoundException;
import com.coupop.fcfscoupon.coupon.model.Coupon;
import com.coupop.fcfscoupon.coupon.model.CouponIssueHistory;
import com.coupop.fcfscoupon.testconfig.IntegrationTestConfig;
import java.time.LocalDate;
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
        final String email = "foo@bar.com";

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

    @DisplayName("이메일로 쿠폰 발급 이력을 조회하여 응답한다.")
    @Test
    void findHistoryByEmail() {
        // given
        final String email = "foo@bar.com";
        couponService.createAndSend(1L, email);
        final HistoryRequest request = new HistoryRequest(email);

        // when
        final HistoryResponse response = couponService.findHistoryByEmail(request);

        // then
        assertThat(response.issuedCoupons()).hasSize(1);
        assertThat(response.issuedCoupons().get(0).date()).isEqualTo(LocalDate.now().toString());
    }

    @DisplayName("이메일로 쿠폰 발급 이력을 조회할때 이력이 존재하지 않으면 예외가 발생한다.")
    @Test
    void findHistoryByEmail_ifHistoryNotFound() {
        // given
        final HistoryRequest request = new HistoryRequest("foo@bar.com");

        assertThatExceptionOfType(HistoryNotFoundException.class)
                .isThrownBy(() -> couponService.findHistoryByEmail(request));
    }
}
