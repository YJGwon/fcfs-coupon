package com.coupop.fcfscoupon.api.coupon.dto;

import com.coupop.fcfscoupon.common.inputvalidation.EmailValidation;
import jakarta.validation.constraints.Email;

public record IssuanceRequest(Long seq,
                              @Email(regexp = EmailValidation.REGEX, message = EmailValidation.MESSAGE) String email) {
}
