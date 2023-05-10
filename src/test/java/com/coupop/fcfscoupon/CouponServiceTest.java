package com.coupop.fcfscoupon;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.BDDMockito.given;

import com.coupop.fcfscoupon.dto.CouponResponse;
import com.coupop.fcfscoupon.execption.CouponNotOpenedException;
import java.time.LocalTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
class CouponServiceTest {

    @Autowired
    private CouponService couponService;

    @MockBean
    private RequestTime requestTime;

    @DisplayName("쿠폰을 발급한다.")
    @Test
    void issue() {
        // given
        final LocalTime openTime = LocalTime.of(10, 0);
        given(requestTime.getValue())
                .willReturn(openTime);

        // when
        CouponResponse coupon = couponService.issue();

        // then
        assertThat(coupon.value()).isNotBlank();
    }

    @DisplayName("쿠폰을 발급할 때 쿠폰이 열려있지 않으면 예외가 발생한다.")
    @Test
    void issue_throwsException_ifCouponIsNotOpen() {
        // given
        final LocalTime closedTime = LocalTime.of(9, 59);
        given(requestTime.getValue())
                .willReturn(closedTime);

        // when & then
        assertThatExceptionOfType(CouponNotOpenedException.class)
                .isThrownBy(() -> couponService.issue());
    }
}
