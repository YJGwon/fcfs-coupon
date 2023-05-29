package com.coupop.fcfscoupon.domain.coupon.model;

import java.util.List;
import java.util.Optional;

public interface CouponIssueHistoryRepository {

    CouponIssueHistory save(final CouponIssueHistory detail);

    List<CouponIssueHistory> findByEmail(final String email);

    Optional<CouponIssueHistory> findById(final String id);
}
