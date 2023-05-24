package com.coupop.coupon.dto;

import com.coupop.coupon.support.EmailValidation;
import jakarta.validation.constraints.Email;

public record HistoryRequest(@Email(regexp = EmailValidation.REGEX, message = EmailValidation.MESSAGE) String email) {
}
