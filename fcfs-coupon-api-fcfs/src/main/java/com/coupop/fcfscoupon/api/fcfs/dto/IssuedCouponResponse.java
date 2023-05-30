package com.coupop.fcfscoupon.api.fcfs.dto;

import com.coupop.fcfscoupon.domain.history.dto.CouponIssueHistoryRecord;

public record IssuedCouponResponse(String id, String date) {

    public static IssuedCouponResponse of(final CouponIssueHistoryRecord history) {
        return new IssuedCouponResponse(history.id(), history.createdAt().toLocalDate().toString());
    }
}
