package com.coupop.fcfscoupon.coupon.model;

import java.time.LocalDate;

public class CouponIssueHistoryDetail {

    private final String couponId;
    private final LocalDate createdAt;

    private CouponIssueHistoryDetail(final String couponId, final LocalDate createdAt) {
        this.couponId = couponId;
        this.createdAt = createdAt;
    }

    public static CouponIssueHistoryDetail ofNew(final Coupon coupon) {
        return new CouponIssueHistoryDetail(coupon.getId(), LocalDate.now());
    }

    public String getCouponId() {
        return couponId;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }
}
