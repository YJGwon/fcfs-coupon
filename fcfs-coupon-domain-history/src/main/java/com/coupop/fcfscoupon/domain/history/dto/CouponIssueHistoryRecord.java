package com.coupop.fcfscoupon.domain.history.dto;

import com.coupop.fcfscoupon.domain.history.model.CouponIssueHistory;
import java.time.LocalDateTime;

public record CouponIssueHistoryRecord(String id, String email, String couponId, LocalDateTime createdAt) {

    public static CouponIssueHistoryRecord of(final CouponIssueHistory couponIssueHistory) {
        return new CouponIssueHistoryRecord(
                couponIssueHistory.getId(),
                couponIssueHistory.getEmail(),
                couponIssueHistory.getCouponId(),
                couponIssueHistory.getCreatedAt()
        );
    }
}
