package com.coupop.fcfscoupon;

import static org.assertj.core.api.Assertions.assertThat;

import com.coupop.fcfscoupon.dto.CouponResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CouponServiceTest {

    @Autowired
    private CouponService couponService;

    @DisplayName("쿠폰을 발급한다.")
    @Test
    void issue() {
        CouponResponse coupon = couponService.issue();
        assertThat(coupon.value()).isNotBlank();
    }
}
