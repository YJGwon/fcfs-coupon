package com.coupop.coupon;

import com.coupop.coupon.model.Coupon;
import com.coupop.coupon.model.CouponEmailSender;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Disabled
@SpringBootTest
class AsyncCouponEmailSenderTest {

    @Autowired
    private CouponEmailSender couponEmailSender;

    @DisplayName("쿠폰을 메일로 발송한다.")
    @Test
    void send() {
        final Coupon coupon = new Coupon("뭔가 좋은 쿠폰");
        final String toAddress = "yj970125@gmail.com";

        couponEmailSender.send(coupon, toAddress);
    }
}
