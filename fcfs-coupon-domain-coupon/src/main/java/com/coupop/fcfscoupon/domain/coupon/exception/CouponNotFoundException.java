package com.coupop.fcfscoupon.domain.coupon.exception;

import com.coupop.fcfscoupon.common.exception.NotFoundException;

public class CouponNotFoundException extends NotFoundException {

    private static final String TITLE = "쿠폰이 존재하지 않습니다.";
    private static final String FORMAT_DETAIL = "해당하는 쿠폰이 존재하지 않습니다. [입력값: %s]";

    public CouponNotFoundException(final String id) {
        super(TITLE, String.format(FORMAT_DETAIL, id));
    }
}
