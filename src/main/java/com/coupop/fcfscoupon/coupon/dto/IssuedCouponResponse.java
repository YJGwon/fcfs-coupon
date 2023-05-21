package com.coupop.fcfscoupon.coupon.dto;

import com.coupop.fcfscoupon.coupon.model.CouponIssueHistoryDetail;

public record IssuedCouponResponse(String id, String date) {

    public static IssuedCouponResponse of(final CouponIssueHistoryDetail historyDetail) {
        return new IssuedCouponResponse(historyDetail.getCouponId(), historyDetail.getCreatedAt().toString());
    }
}
