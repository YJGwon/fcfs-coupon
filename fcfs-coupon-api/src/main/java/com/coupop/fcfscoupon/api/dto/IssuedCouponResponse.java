package com.coupop.fcfscoupon.api.dto;

import com.coupop.fcfscoupon.domain.coupon.model.CouponIssueHistory;

public record IssuedCouponResponse(String id, String date) {

    public static IssuedCouponResponse of(final CouponIssueHistory history) {
        return new IssuedCouponResponse(history.getId(), history.getCreatedAt());
    }
}
