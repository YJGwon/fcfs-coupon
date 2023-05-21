package com.coupop.fcfscoupon.coupon.exception;

import com.coupop.fcfscoupon.exception.ApiException;
import org.springframework.http.HttpStatus;

public class HistoryNotFoundException extends ApiException {

    private static final String TITLE = "쿠폰 발급 이력이 존재하지 않습니다.";
    private static final String FORMAT_DETAIL = "이메일 대해 발급된 쿠폰이 존재하지 않습니다. [입력값: %s]";

    public HistoryNotFoundException(final String email) {
        super(TITLE, String.format(FORMAT_DETAIL, email), HttpStatus.NOT_FOUND);
    }
}
