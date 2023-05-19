package com.coupop.fcfscoupon.coupon.model;

import java.util.List;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class CouponIssueHistory {

    private final String email;

    private final List<CouponIssueHistoryDetail> issuedCoupons;

    public CouponIssueHistory(final String email,
                              final List<CouponIssueHistoryDetail> issuedCoupons) {
        this.email = email;
        this.issuedCoupons = issuedCoupons;
    }

    public String getEmail() {
        return email;
    }

    public List<CouponIssueHistoryDetail> getIssuedCoupons() {
        return issuedCoupons;
    }
}
