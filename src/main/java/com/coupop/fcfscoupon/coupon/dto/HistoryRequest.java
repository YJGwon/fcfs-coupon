package com.coupop.fcfscoupon.coupon.dto;

import com.coupop.fcfscoupon.support.EmailValidation;
import jakarta.validation.constraints.Email;

public record HistoryRequest(@Email(regexp = EmailValidation.REGEX, message = EmailValidation.MESSAGE) String email) {
}
