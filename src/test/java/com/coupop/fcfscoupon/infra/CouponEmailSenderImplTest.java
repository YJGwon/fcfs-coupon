package com.coupop.fcfscoupon.infra;

import com.coupop.fcfscoupon.model.Coupon;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CouponEmailSenderImplTest {

    @Autowired
    private CouponEmailSenderImpl couponEmailSender;

    @Disabled
    @DisplayName("쿠폰을 메일로 발송한다.")
    @Test
    void send() {
        final Coupon coupon = new Coupon("쿠폰");
        final String toAddress = "yj970125@gmail.com";

        couponEmailSender.send(coupon, toAddress);
    }
}