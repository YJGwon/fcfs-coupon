package com.coupop.core.exception;

import org.springframework.http.HttpStatus;

public class BadRequestException extends ApiException {

    public BadRequestException(final String title, final String detail) {
        super(title, detail, HttpStatus.BAD_REQUEST);
    }
}
