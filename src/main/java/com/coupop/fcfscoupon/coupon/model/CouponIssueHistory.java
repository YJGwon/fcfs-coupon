package com.coupop.fcfscoupon.coupon.model;

import java.time.LocalDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class CouponIssueHistory {

    private final String email;
    private final Coupon coupon;
    private final LocalDate createdAt;

    @Id
    private String id;

    private CouponIssueHistory(final String email, final Coupon coupon, final LocalDate createdAt) {
        this.email = email;
        this.coupon = coupon;
        this.createdAt = createdAt;
    }

    public static CouponIssueHistory ofNew(final String email, final Coupon coupon) {
        return new CouponIssueHistory(email, coupon, LocalDate.now());
    }

    public String getEmail() {
        return email;
    }

    public Coupon getCoupon() {
        return coupon;
    }

    public String getCreatedAt() {
        return createdAt.toString();
    }
}
