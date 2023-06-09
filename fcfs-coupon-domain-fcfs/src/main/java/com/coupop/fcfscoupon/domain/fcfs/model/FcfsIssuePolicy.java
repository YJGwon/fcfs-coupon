package com.coupop.fcfscoupon.domain.fcfs.model;

import java.time.LocalTime;

public class FcfsIssuePolicy {

    private static final LocalTime OPEN_AT = LocalTime.of(10, 0);

    private static final int LIMIT = 50;

    public static boolean isCouponClosed(LocalTime time) {
        return time.isBefore(OPEN_AT);
    }

    public static boolean isCouponOutOfStock(int issuedCount) {
        return issuedCount >= LIMIT;
    }

    public static boolean isCouponOverIssued(Long issuedCount) {
        return issuedCount > LIMIT;
    }

    public static LocalTime getOpenAt() {
        return OPEN_AT;
    }

    public static int getLimit() {
        return LIMIT;
    }
}
