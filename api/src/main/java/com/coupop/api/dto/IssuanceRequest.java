package com.coupop.api.dto;

import com.coupop.core.inputvalidation.EmailValidation;
import jakarta.validation.constraints.Email;

public record IssuanceRequest(@Email(regexp = EmailValidation.REGEX, message = EmailValidation.MESSAGE) String email) {
}
