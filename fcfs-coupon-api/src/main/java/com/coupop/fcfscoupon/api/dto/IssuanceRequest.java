package com.coupop.fcfscoupon.api.dto;

import com.coupop.fcfscoupon.common.inputvalidation.EmailValidation;
import jakarta.validation.constraints.Email;

public record IssuanceRequest(@Email(regexp = EmailValidation.REGEX, message = EmailValidation.MESSAGE) String email) {
}
