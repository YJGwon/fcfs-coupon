package com.coupop.fcfscoupon.fcfsissue.dto;

import com.coupop.fcfscoupon.support.EmailValidation;
import jakarta.validation.constraints.Email;

public record IssuanceRequest(@Email(regexp = EmailValidation.REGEX, message = EmailValidation.MESSAGE) String email) {
}
