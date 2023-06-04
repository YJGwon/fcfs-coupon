package com.coupop.fcfscoupon.domain.history.model;

import java.time.LocalDateTime;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Data
public class CouponIssueHistory {

    private final String email;
    private final String couponId;
    private final LocalDateTime createdAt;

    @Id
    private String id;

    public static CouponIssueHistory ofNew(final String email, final String couponId) {
        return new CouponIssueHistory(email, couponId, LocalDateTime.now());
    }
}
