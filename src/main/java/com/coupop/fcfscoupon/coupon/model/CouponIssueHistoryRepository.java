package com.coupop.fcfscoupon.coupon.model;

public interface CouponIssueHistoryRepository {

    long save(final String email, final CouponIssueHistoryDetail detail);

    CouponIssueHistory findByEmail(final String email);
}
