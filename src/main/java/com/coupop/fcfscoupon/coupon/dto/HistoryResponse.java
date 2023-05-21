package com.coupop.fcfscoupon.coupon.dto;

import com.coupop.fcfscoupon.coupon.model.CouponIssueHistory;
import java.util.List;

public record HistoryResponse(List<IssuedCouponResponse> issuedCoupons) {

    public static HistoryResponse of(final List<CouponIssueHistory> history) {
        List<IssuedCouponResponse> issuedCoupons = history.stream()
                .map(IssuedCouponResponse::of)
                .toList();
        return new HistoryResponse(issuedCoupons);
    }
}
