package com.coupop.fcfscoupon.dto;

import jakarta.validation.constraints.Email;

public record CouponRequest(@Email(regexp = REGEX_EMAIL, message = MESSAGE_INVALID) String email) {

    private static final String REGEX_EMAIL = "^[a-zA-Z0-9+-\\_.]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$";
    private static final String MESSAGE_INVALID = "형식에 맞는 이메일을 입력하세요.";
}
