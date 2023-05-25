package com.coupop.fcfscoupon.domain.coupon.model;

public interface CouponEmailSender {

    void send(final Coupon coupon, final String toAddress);
}
