package com.coupop.fcfscoupon.dto;

import com.coupop.fcfscoupon.model.Coupon;

public record CouponResponse(String value) {

    public static CouponResponse of(final Coupon coupon) {
        return new CouponResponse(coupon.getValue());
    }
}
