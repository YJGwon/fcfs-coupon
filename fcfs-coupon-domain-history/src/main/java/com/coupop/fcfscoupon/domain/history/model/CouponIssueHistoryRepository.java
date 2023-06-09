package com.coupop.fcfscoupon.domain.history.model;

import java.util.List;
import java.util.Optional;

public interface CouponIssueHistoryRepository {

    CouponIssueHistory save(final CouponIssueHistory history);

    List<CouponIssueHistory> findByEmail(final String email);

    Optional<CouponIssueHistory> findById(final String id);
}
