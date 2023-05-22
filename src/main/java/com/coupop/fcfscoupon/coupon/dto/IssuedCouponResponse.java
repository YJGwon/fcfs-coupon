package com.coupop.fcfscoupon.coupon.dto;

import com.coupop.fcfscoupon.coupon.model.CouponIssueHistory;

public record IssuedCouponResponse(String id, String date) {

    public static IssuedCouponResponse of(final CouponIssueHistory history) {
        return new IssuedCouponResponse(history.getId(), history.getCreatedAt());
    }
}
