package com.coupop.fcfscoupon.coupon.model;

import java.util.Optional;

public interface CouponIssueHistoryRepository {

    long save(final String email, final CouponIssueHistoryDetail detail);

    Optional<CouponIssueHistory> findByEmail(final String email);
}
