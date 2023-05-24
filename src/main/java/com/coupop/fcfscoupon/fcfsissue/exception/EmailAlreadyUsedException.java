package com.coupop.fcfscoupon.fcfsissue.exception;

import com.coupop.core.exception.BadRequestException;

public class EmailAlreadyUsedException extends BadRequestException {

    private static final String TITLE = "이미 사용된 이메일입니다.";
    private static final String FORMAT_DETAIL = "한 이메일 대해 하루 한 장의 쿠폰만 발급받을 수 있습니다. [입력값: %s]";

    public EmailAlreadyUsedException(final String email) {
        super(TITLE, String.format(FORMAT_DETAIL, email));
    }
}
