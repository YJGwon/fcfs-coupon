package com.coupop.core.exception;

import org.springframework.http.HttpStatus;

public class NotFoundException extends ApiException {

    public NotFoundException(final String title, final String detail) {
        super(title, detail, HttpStatus.NOT_FOUND);
    }
}
