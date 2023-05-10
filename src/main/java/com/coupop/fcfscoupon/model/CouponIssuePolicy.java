package com.coupop.fcfscoupon.model;

import java.time.LocalTime;

public class CouponIssuePolicy {

    private static final LocalTime openAt = LocalTime.of(10, 0);

    public static boolean isCouponClosed(LocalTime time) {
        return time.isBefore(openAt);
    }

    public static LocalTime getOpenAt() {
        return openAt;
    }
}
