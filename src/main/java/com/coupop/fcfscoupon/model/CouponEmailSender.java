package com.coupop.fcfscoupon.model;

public interface CouponEmailSender {

    void send(final Coupon coupon, final String toAddress);
}
