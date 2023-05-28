package com.coupop.fcfscoupon.domain.history.model;

import java.time.LocalDateTime;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class CouponIssueHistory {

    private final String email;
    private final String couponId;
    private final LocalDateTime createdAt;

    @Id
    private String id;

    private CouponIssueHistory(final String email, final String couponId, final LocalDateTime createdAt) {
        this.email = email;
        this.couponId = couponId;
        this.createdAt = createdAt;
    }

    public static CouponIssueHistory ofNew(final String email, final String couponId) {
        return new CouponIssueHistory(email, couponId, LocalDateTime.now());
    }

    public String getId() {
        return id;
    }

    public String getCouponId() {
        return couponId;
    }

    public String getEmail() {
        return email;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
