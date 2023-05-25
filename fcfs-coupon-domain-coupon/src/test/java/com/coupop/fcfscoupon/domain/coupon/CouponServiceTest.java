package com.coupop.fcfscoupon.domain.coupon;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

import com.coupop.fcfscoupon.domain.coupon.exception.HistoryNotFoundException;
import com.coupop.fcfscoupon.domain.coupon.model.Coupon;
import com.coupop.fcfscoupon.domain.coupon.model.CouponIssueHistory;
import com.coupop.fcfscoupon.domain.coupon.testconfig.CouponIntegrationTestConfig;
import java.time.LocalDate;
import java.util.List;
import java.util.function.Function;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;


class CouponServiceTest extends CouponIntegrationTestConfig {

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

        // when
        final List<CouponIssueHistory> histories = couponService.findHistoryByEmail(email, Function.identity());

        // then
        assertThat(histories).hasSize(1);
        assertThat(histories.get(0).getCreatedAt()).isEqualTo(LocalDate.now().toString());
    }

    @DisplayName("이메일로 쿠폰 발급 이력을 조회할때 이력이 존재하지 않으면 예외가 발생한다.")
    @Test
    void findHistoryByEmail_ifHistoryNotFound() {
        assertThatExceptionOfType(HistoryNotFoundException.class)
                .isThrownBy(() -> couponService.findHistoryByEmail("foo@bar.com", Function.identity()));
    }

    @DisplayName("이미 발급된 쿠폰에 대해 재전송을 요청하면 같은 이메일로 재전송한다.")
    @Test
    void resend() {
        // given
        final String email = "foo@bar.com";
        couponService.createAndSend(1L, email);
        final CouponIssueHistory savedHistory = mongoTemplate
                .findOne(query(where("email").is(email)), CouponIssueHistory.class);

        // when & then
        assertAll(
                () -> assertThatNoException()
                        .isThrownBy(() -> couponService.resend(savedHistory.getId())),
                () -> verify(couponEmailSender, times(2)).send(any(Coupon.class), eq(email))
        );
    }

    @DisplayName("재전송을 요청할 때 해당하는 발급 이력이 없을 경우 예외가 발생한다.")
    @Test
    void resend_ifHistoryNotFound() {
        assertThatExceptionOfType(HistoryNotFoundException.class)
                .isThrownBy(() -> couponService.resend("invalidId"));
    }
}
