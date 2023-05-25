package com.coupop.api.dto;

import com.coupop.fcfscoupon.domain.coupon.model.CouponIssueHistory;
import java.util.List;

public record HistoryResponse(List<IssuedCouponResponse> issuedCoupons) {

    public static HistoryResponse of(final List<CouponIssueHistory> history) {
        List<IssuedCouponResponse> issuedCoupons = history.stream()
                .map(IssuedCouponResponse::of)
                .toList();
        return new HistoryResponse(issuedCoupons);
    }
}
