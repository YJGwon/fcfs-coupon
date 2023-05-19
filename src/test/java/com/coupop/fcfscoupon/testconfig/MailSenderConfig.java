package com.coupop.fcfscoupon.testconfig;

import com.coupop.fcfscoupon.coupon.model.CouponEmailSender;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class MailSenderConfig {

    @Bean
    CouponEmailSender couponEmailSender() {
        return (coupon, toAddress) -> {
        };
    }
}
