package com.coupop.fcfscoupon.coupon.exception;

import com.coupop.fcfscoupon.exception.ApiException;
import org.springframework.http.HttpStatus;

public class HistoryNotFoundException extends ApiException {

    private static final String TITLE = "쿠폰 발급 이력이 존재하지 않습니다.";
    private static final String FORMAT_DETAIL_EMAIL = "이메일 대해 발급된 쿠폰이 존재하지 않습니다. [입력값: %s]";
    private static final String FORMAT_DETAIL_ID = "해당하는 발급 이력이 존재하지 않습니다. [입력값: %s]";

    private HistoryNotFoundException(final String detail) {
        super(TITLE, detail, HttpStatus.NOT_FOUND);
    }

    public static HistoryNotFoundException ofEmail(final String email) {
        return new HistoryNotFoundException(String.format(FORMAT_DETAIL_EMAIL, email));
    }

    public static HistoryNotFoundException ofId(final String id) {
        return new HistoryNotFoundException(String.format(FORMAT_DETAIL_ID, id));
    }
}
