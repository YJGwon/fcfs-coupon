package com.coupop.fcfscoupon.api.fcfs.dto;

import com.coupop.fcfscoupon.domain.history.dto.CouponIssueHistoryRecord;
import java.util.List;

public record HistoryResponse(List<IssuedCouponResponse> issuedCoupons) {

    public static HistoryResponse of(final List<CouponIssueHistoryRecord> history) {
        List<IssuedCouponResponse> issuedCoupons = history.stream()
                .map(IssuedCouponResponse::of)
                .toList();
        return new HistoryResponse(issuedCoupons);
    }
}
