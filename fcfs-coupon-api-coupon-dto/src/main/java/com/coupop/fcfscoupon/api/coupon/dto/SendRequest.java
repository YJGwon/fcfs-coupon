package com.coupop.fcfscoupon.api.coupon.dto;

import com.coupop.fcfscoupon.common.inputvalidation.EmailValidation;
import jakarta.validation.constraints.Email;

public record SendRequest(String couponId,
                          @Email(regexp = EmailValidation.REGEX, message = EmailValidation.MESSAGE) String email) {
}
